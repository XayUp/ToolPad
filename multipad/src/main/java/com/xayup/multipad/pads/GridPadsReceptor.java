package com.xayup.multipad.pads;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.xayup.debug.XLog;
import com.xayup.multipad.pads.Render.MakePads;
import com.xayup.multipad.pads.Render.PadSkinData;
import com.xayup.multipad.projects.Project;
import com.xayup.multipad.projects.ProjectManager;
import com.xayup.multipad.projects.project.autoplay.AutoPlay;
import com.xayup.multipad.projects.project.keyled.KeyLED;
import com.xayup.multipad.projects.project.keysounds.KeySounds;
import com.xayup.multipad.projects.thread.KeyLedThread;
import com.xayup.multipad.skin.SkinManager;
import com.xayup.multipad.skin.SkinProperties;
import com.xayup.multipad.skin.SkinSupport;

import java.util.*;

public abstract class GridPadsReceptor {
    protected Activity context;
    protected Map<Integer, List<String>> project_use_grid; //Get grid by project id
    protected Map<String, PadGrid> grids; //Get grid by name
    protected Map<Integer, List<String>> grid_ids; //Get grid by ID
    protected KeyLedThread keyLedThread;

    public SkinManager mSkinManager;
    protected PadGrid active_pad = null;
    protected View.OnTouchListener resize_touch;
    protected View.OnTouchListener rotate_touch;
    protected View.OnTouchListener move_touch;
    /*Shared*/
    public MakePads.ChainInfo current_chain;
    public boolean watermark_press = false;
    public boolean watermark = true;

    public interface PadLayoutMode {
        byte LAYOUT_PRO_MODE = 0;
        byte LAYOUT_MK2_MODE = 1;
        byte LAYOUT_UNIPAD_MODE = 2;
        byte LAYOUT_MATRIX_MODE = 3;
    }

    public GridPadsReceptor(Context context) {
        this.context = (Activity) context;
        this.mSkinManager = new SkinManager();
        this.current_chain = new MakePads.ChainInfo(1, 9);
        this.grids = new HashMap<>();
        (this.keyLedThread = new KeyLedThread()).loop();
    }

    /**
     * Chamado quando algum botão é clicado em uma determinada grade
     * @param grid a grade do botão.
     * @param pad o botão
     * @param event o evento de toque
     */
    public abstract boolean onTheButtonSign(PadGrid grid, MakePads.PadInfo pad, MotionEvent event);

    public abstract void onGridCreated(PadGrid padGrid);
    public abstract void onGridDeleted();

    /**
     * Make new Pads Object. Get this or last created pad with getActivePads()
     * @param skin_package : SKin name to get and set on the Pads
     * @param rows : rows count
     * @param columns : columns count.
     */
    public void newPads(String skin_package, byte rows, byte columns, int[] color_table) {
        PadGrid padGrid = new PadGrid(SkinManager.getSkinProperties(context, skin_package), rows, columns);
        padGrid.setName("Grid_" + grids.size());
        padGrid.setId(0);
        grids.put(padGrid.getName(), padGrid);
        active_pad = padGrid;
        padGrid.setColors(color_table);
        onGridCreated(active_pad);
    }

    /**
     * Chame isto sempre que o estado de um projeto for alterado
     * @param projectManager o projeto a qual o estado foi recém alterado
     */
    public void notifyProject(ProjectManager projectManager){
        if(projectManager.getProject().getStatus() == Project.STATUS_LOADED) {
            keyLedThread.addCallback(projectManager);
            if(projectManager.getKeyLED() != null) projectManager.getKeyLED().setKeyLedThread(keyLedThread);
        } else {
            keyLedThread.removeCallback(projectManager);
        }
    }
    
    public List<String> getPadsWithId(int id){
        return grid_ids.get(id);
    }
    public PadGrid getGridByName(String name){
        return grids.get(name);
    }

    public List<PadGrid> getAllPadsList(){ return new ArrayList<>(grids.values()); }
    public List<String> getAllPadsNameList(){ return new ArrayList<>(grids.keySet()); }


    /**
     * Isso retorna a Pads ativa (Que será definido pelo usuário ou quando uma nova Pads é criada)
     * @return um Objeto Pads atual
     */
    public PadGrid getActivePads(){ return active_pad; }

    public class PadGrid implements PadsLayoutInterface, SkinSupport {
        protected MakePads.ChainInfo current_chain;
        protected CurrentProject current_project;
        protected String name;
        protected byte layout_mode;
        protected SkinProperties mSkinProperties;
        protected PadSkinData mSkinData;
        protected ImageView pad_background;
        protected RelativeLayout container;
        protected RelativeLayout mRootPads;
        protected RelativeLayout pads_settings_overlay;
        protected GridLayout mGrid;
        protected int lp_id;
        protected MakePads.Pads mPads;
        protected int[] colors;
        protected boolean reverse_colum;
        protected boolean reverse_row;

        public PadGrid(SkinProperties skin, byte rows, byte columns) {
            this.mSkinProperties = skin;
            this.mSkinData = new PadSkinData();
            this.current_project = new CurrentProject();

            this.current_chain = new MakePads.ChainInfo(1, 9);

            this.mGrid = (this.mPads = new MakePads(context).make(rows, columns)).getGrid();

            this.pad_background = new ImageView(context);
            this.pad_background.setScaleType(ImageView.ScaleType.CENTER_CROP);
            (this.mRootPads = new RelativeLayout(context)).addView(pad_background, new ViewGroup.LayoutParams(-1, -1));
            this.mRootPads.addView(this.mGrid, new ViewGroup.LayoutParams(-1, -1));
            this.pads_settings_overlay = new RelativeLayout(context);
            this.mRootPads.addView(this.pads_settings_overlay, new ViewGroup.LayoutParams(-1, -1));
            //this.mRootPads.setClipChildren(false);

            this.container = new RelativeLayout(context);
            this.container.addView(this.mRootPads);
            this.container.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
            //this.container.setClipChildren(false);

            this.reverse_row = false;
            this.reverse_colum = false;

            this.layout_mode = PadLayoutMode.LAYOUT_PRO_MODE;
            this.lp_id = 0;
            applySkin(this.mSkinProperties);
            forAllPads(
                (pad, mPadGrid) -> mPadGrid.getPads().getPadView(pad.getRow(), pad.getColum()).setOnTouchListener(
                    (pad_view, event) -> {
                        pad_view.performClick();
                        return onTheButtonSign(mPadGrid, pad, event);
                    }
                )
            );
        }

        public void setColors(int[] colors){ this.colors = colors; }
        public int[] getColors(){ return this.colors; }

        //// INTERACTION ////
        public void led(int row, int colum, int color){
            mPads.setLedColor(
                    (reverse_row) ? 9 + (row * (-1)) : row,
                    (reverse_colum) ? 9 + (colum * (-1)) : colum,
                    (color < 0) ? color: colors[color]);
        }

        //// INFORMATION'S ////
        public MakePads.ChainInfo getCurrentChain() {
            return current_chain;
        }

        public MakePads.Pads getPads(){ return mPads; }
        public int getId(){ return this.lp_id; }
        public void setName(String name){ this.name = name; }
        public String getName(){ return this.name; }
        public boolean isReversedRow(){ return reverse_row; }
        public boolean isReversedColum(){ return reverse_colum; }

        public void reverseRow(boolean inverse){ this.reverse_row = inverse; }
        public void reverseColum(boolean inverse){ this.reverse_colum = inverse; }

        public void setId(int id){
            /*
            if(grid_ids.get(lp_id) != null) grid_ids.get(lp_id).remove(name);
            if(grid_ids.get(id) != null){
                Objects.requireNonNull(grid_ids.get(id)).add(name);
            } else {
                grid_ids.put(id, new ArrayList<>(List.of(name)));
            }
            */
            if(current_project.getProjectManager() != null){
                current_project.getProjectManager().removeGrid(this);
                lp_id = id;
                current_project.getProjectManager().addGrid(this);
            } else { lp_id = id; }
        }

        //// MANAGER ////
        public void removeThis(){
            Objects.requireNonNull(grid_ids.get(lp_id)).remove(name);
        }
        public void forAllPads(ForAllPads fap) {
            for (int i = 0; i < mGrid.getChildCount(); i++) {
                if (mGrid.getChildAt(i).getTag() instanceof MakePads.PadInfo) {
                    fap.run((MakePads.PadInfo) mGrid.getChildAt(i).getTag(), this);
                }
            }
        }
        public void setCurrentChain(int row, int colum){
            //Disable old chain
            this.getPads().getPadInfo(current_chain.getRow(), current_chain.getColum()).markAsActivated(false);
            this.led(current_chain.getRow(), current_chain.getColum(), 0);
            //Enable new chain
            this.current_chain.setCurrentChain(row, colum);
            this.getPads().getPadInfo(current_chain.getRow(), current_chain.getColum()).markAsActivated(true);
            this.led(current_chain.getRow(), current_chain.getColum(), 0);
        }

        //// PROJECT ////
        public void setProject(ProjectManager projectManager){
            this.current_project.setProjectManager(projectManager);
            this.current_project.getProjectManager().addGrid(this);
        }
        public CurrentProject getProject(){ return this.current_project; }
        public void removeProject(){
            if(this.current_project.getProjectManager() != null) {
                this.current_project.getProjectManager().removeGrid(this);
                this.current_project.setProjectManager(null);
            }
        }

        //// LAYOUT ////
        public byte getLayoutMode(){
            return layout_mode;
        }

        //// CONTAINERS ////
        public RelativeLayout getContainer(){
            return this.container;
        }
        public RelativeLayout getPadsSettingsOverlay(){
            return pads_settings_overlay;
        }

        @Override
        public PadSkinData getSkinData() {
            return mSkinData;
        }

        @Override
        public RelativeLayout getRootPads() {
            return mRootPads;
        }

        @Override
        public GridLayout getGridPads() {
            return mGrid;
        }

        @Override
        public boolean applySkin(SkinProperties mSkinProperties) {
            mSkinManager.loadSkin(
                    context,
                    mSkinProperties,
                    mSkinData,
                    (skin) -> {
                        ((ImageView) mRootPads.getChildAt(0)).setImageDrawable(mSkinData.draw_playbg);
                        forAllPads(
                                (padInfo, padGrid) -> {
                                    ViewGroup pad = ((ViewGroup) padGrid.getPads().getPadView(padInfo.getRow(), padInfo.getColum()));
                                    for (int ii = 0; ii < pad.getChildCount(); ii++) {
                                        View view = pad.getChildAt(ii);
                                        if(view.getTag() == null) continue;
                                        byte type = (byte) view.getTag();
                                        switch (type) {
                                            case MakePads.PadInfo.PadInfoIdentifier.BTN:
                                                {
                                                    ((ImageView) view).setImageDrawable(null);
                                                    ((ImageView) view)
                                                            .setImageDrawable(mSkinData.draw_btn);
                                                    break;
                                                }
                                            case MakePads.PadInfo.PadInfoIdentifier.BTN_:
                                                {
                                                    ((ImageView) view).setImageDrawable(null);
                                                    ((ImageView) view)
                                                            .setImageDrawable(mSkinData.draw_btn_);
                                                    break;
                                                }
                                            case MakePads.PadInfo.PadInfoIdentifier.PHANTOM:
                                                {
                                                    ((ImageView) view).setImageDrawable(null);
                                                    ((ImageView) view)
                                                            .setImageDrawable(
                                                                    mSkinData.draw_phantom);
                                                    padGrid.getPads().getLed(padInfo.getRow(), padInfo.getColum()).setImageDrawable(mSkinData.draw_phantom_led.mutate().getConstantState().newDrawable());
                                                    break;
                                                }
                                            case MakePads.PadInfo.PadInfoIdentifier.PHANTOM_:
                                                {
                                                    ((ImageView) view).setImageResource(0);
                                                    ((ImageView) view)
                                                            .setImageDrawable(
                                                                    mSkinData.draw_phantom_);
                                                    padGrid.getPads().getLed(padInfo.getRow(), padInfo.getColum()).setImageDrawable(mSkinData.draw_phantom__led.mutate().getConstantState().newDrawable());
                                                    break;
                                                }
                                            case MakePads.PadInfo.PadInfoIdentifier.CHAIN_LED:
                                                {
                                                    ((ImageView) view).setImageDrawable(null);
                                                    ((ImageView) view)
                                                            .setImageDrawable(
                                                                    mSkinData.draw_chainled);
                                                    padGrid.getPads().getLed(padInfo.getRow(), padInfo.getColum()).setImageDrawable(mSkinData.draw_chain_led.mutate().getConstantState().newDrawable());
                                                    break;
                                                }
                                            case MakePads.PadInfo.PadInfoIdentifier.LOGO:
                                                {
                                                    ((ImageView) view).setImageDrawable(null);
                                                    ((ImageView) view)
                                                            .setImageDrawable(mSkinData.draw_logo);
                                                    padGrid.getPads().getLed(padInfo.getRow(), padInfo.getColum()).setImageDrawable(mSkinData.draw_logo_led.mutate().getConstantState().newDrawable());
                                                    break;
                                                }
                                        }
                                    }
                                });
                    });
            return false;
        }

        public class CurrentProject{
            protected ProjectManager projectManager;

            public void setProjectManager(ProjectManager projectManager){
                this.projectManager = projectManager;
            }
            public ProjectManager getProjectManager(){
                return this.projectManager;
            }
            public AutoPlay getAutoPlay(){
                return (projectManager != null) ? projectManager.getAutoPlay() : null;
            }
            public KeyLED getKeyLED(){
                return (projectManager != null) ? projectManager.getKeyLED() : null;
            }
            public KeySounds getKeySounds(){
                return (projectManager != null) ? projectManager.getKeySounds() : null;
            }
            public void callPress(PadGrid padGrid, MakePads.PadInfo pad){
                if(getPadPress() != null) projectManager.getPadPressCall().call(padGrid, pad);
            }
            public void setCurrentChain(int row, int colum){
                if(getProjectManager() != null){
                    List<PadGrid> padGrids = getProjectManager().getGridsFromId(getId());
                    if(padGrids != null) for(PadGrid padGrid : padGrids){
                        padGrid.getCurrentChain().setCurrentChain(row, colum);
                    }
                } else {
                    getCurrentChain().setCurrentChain(row, colum);
                }
            }
            public int getId(){
                return (projectManager == null) ? -1: projectManager.getProject().getProjectId();
            }
            public PadPressCall getPadPress(){
                if (projectManager != null) {
                    XLog.e("getCallPress: CurrentProject", "Success");
                    return projectManager.getPadPressCall();
                } else {
                    XLog.e("getCallPress: CurrentProject", "Error");
                    return null;
                }
            }

        }
    }

    public interface ForAllPads {
        void run(MakePads.PadInfo padInfo, PadGrid padGrid);
    }
}
