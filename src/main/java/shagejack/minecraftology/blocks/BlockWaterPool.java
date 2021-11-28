package shagejack.minecraftology.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.includes.MCLBlock;
import shagejack.minecraftology.blocks.includes.MCLBlockContainer;
import shagejack.minecraftology.tile.TileEntityForge;
import shagejack.minecraftology.util.MachineHelper;

public class BlockWaterPool extends MCLBlock {

    private static final ItemStack[] toolStackList = {
        new ItemStack(Minecraftology.ITEMS.iron_cluster)
    };

    private static final int[][] shapeList = {
            //big plate
            {8,8},
            //ingot
            {8,5},
            //small plate
            {5,5}
    };

    public BlockWaterPool(Material material, String name) {
        super(material, name);
        setSoundType(SoundType.ANVIL);
        setHarvestLevel("pickaxe", 1);
        setHardness(4.0F);
        setResistance(4.0F);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn instanceof FakePlayer || playerIn == null)
            return false;

            ItemStack held = playerIn.getHeldItem(EnumHand.MAIN_HAND);
            if (isValidCoolItem(held, playerIn)) {
                if (Minecraftology.ITEMS.iron_cluster.getTemp(held) > 873.15) {
                    getCoolResult(held);
                    return true;
                }
            }

        return false;
    }

    public static boolean isValidCoolItem(ItemStack stack, EntityPlayer player) {
        return !stack.isEmpty() && (isCoolItemWhitelisted(stack));
    }

    private static boolean isCoolItemWhitelisted(ItemStack stack) {
        for (ItemStack itemStack : toolStackList) {
            if (ItemStack.areItemsEqualIgnoreDurability(itemStack, stack))
                return true;
        }
        return false;
    }

    public void getCoolResult(ItemStack itemStack){
        ItemStack coolResultStack;
        double mass = Minecraftology.ITEMS.iron_cluster.getMass(itemStack);
        double carbon = Minecraftology.ITEMS.iron_cluster.getCarbon(itemStack);
        double impurities = Minecraftology.ITEMS.iron_cluster.getImpurities(itemStack);
        int[] shape = Minecraftology.ITEMS.iron_cluster.getShape(itemStack);

        if(impurities < 0.01) {
            if(carbon < 0.002) {
                //wrought iron
                if(shape == shapeList[0]){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.wrought_iron_big_plate);
                    Minecraftology.ITEMS.wrought_iron_big_plate.setMass(coolResultStack, mass);
                } else if(shape == shapeList[1]){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.wrought_iron_ingot);
                    Minecraftology.ITEMS.wrought_iron_ingot.setMass(coolResultStack, mass);
                } else if(shape == shapeList[2]){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.wrought_iron_small_plate);
                    Minecraftology.ITEMS.wrought_iron_small_plate.setMass(coolResultStack, mass);
                } else {
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_rubbish);
                }
            } else if (carbon < 0.02){
                //iron
                if(shape == shapeList[0]){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_big_plate);
                    Minecraftology.ITEMS.iron_big_plate.setMass(coolResultStack, mass);
                } else if(shape == shapeList[1]){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_ingot);
                    Minecraftology.ITEMS.iron_ingot.setMass(coolResultStack, mass);
                } else if(shape == shapeList[2]){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_small_plate);
                    Minecraftology.ITEMS.iron_small_plate.setMass(coolResultStack, mass);
                } else {
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_rubbish);
                }
            } else if (carbon < 0.043){
                //pig iron
                if(shape == shapeList[0]){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.pig_iron_big_plate);
                    Minecraftology.ITEMS.pig_iron_big_plate.setMass(coolResultStack, mass);
                } else if(shape == shapeList[1]){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.pig_iron_ingot);
                    Minecraftology.ITEMS.pig_iron_ingot.setMass(coolResultStack, mass);
                } else if(shape == shapeList[2]){
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.pig_iron_small_plate);
                    Minecraftology.ITEMS.pig_iron_small_plate.setMass(coolResultStack, mass);
                } else {
                    coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_rubbish);
                }
            } else {
                //rubbish
                coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_rubbish);
            }
        } else {
            //rubbish
            coolResultStack = new ItemStack(Minecraftology.ITEMS.iron_rubbish);
        }

        itemStack = coolResultStack;
        //TODO:Play Sound
    }


}
