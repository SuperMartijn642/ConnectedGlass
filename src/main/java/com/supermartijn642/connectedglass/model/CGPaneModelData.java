package com.supermartijn642.connectedglass.model;

import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.Map;

/**
 * Created 5/13/2020 by SuperMartijn642
 */
public class CGPaneModelData extends CGModelData {

    public Map<EnumFacing,Boolean> up = new HashMap<>(), down = new HashMap<>();
    public boolean upPost, downPost;

}
