package shagejack.shagecraft.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.data.Inventory;
import shagejack.shagecraft.data.inventory.Slot;
import shagejack.shagecraft.machines.ShageTileEntityMachine;
import shagejack.shagecraft.machines.MachineNBTCategory;
import shagejack.shagecraft.machines.events.MachineEvent;

import java.util.EnumSet;

public class TileEntityForge extends ShageTileEntityMachine {

    public static int forge_slot;

    public TileEntityForge() {
        super();
    }

    @Override
    protected void RegisterSlots(Inventory inventory) {
        forge_slot = inventory.AddSlot(new Slot(true));
        super.RegisterSlots(inventory);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return true;
    }

    @Override
    public boolean getServerActive() {
        return true;
    }

    @Override
    public float soundVolume() {
        return 0.3f;
    }

    @Override
    public void onAdded(World world, BlockPos pos, IBlockState state) {
    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed(World worldIn, BlockPos pos, IBlockState state) {
    }

    @Override
    public void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock) {

    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {

    }

    public void onChunkUnload() {
    }

    @Override
    protected void onAwake(Side side) {
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (categories.contains(MachineNBTCategory.INVENTORY) && toDisk) {
            inventory.writeToNBT(nbt, true);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.INVENTORY)) {
            inventory.readFromNBT(nbt);
        }
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {
    }

    public void forge(EntityPlayer player, ItemStack held){

        if(held.getItemDamage() <= held.getMaxDamage()) {


            ItemStack itemStack = inventory.getSlot(0).getItem();
            if (itemStack.getItem() == Shagecraft.ITEMS.iron_cluster) {


                boolean heavyHit = player.isSneaking();
                double mass = Shagecraft.ITEMS.iron_cluster.getMass(itemStack);
                double carbon = Shagecraft.ITEMS.iron_cluster.getCarbon(itemStack);
                double impurities = Shagecraft.ITEMS.iron_cluster.getImpurities(itemStack);
                double temp = Shagecraft.ITEMS.iron_cluster.getTemp(itemStack);
                int[] shape = Shagecraft.ITEMS.iron_cluster.getShape(itemStack);

                if (temp > 873.15){

                    if (heavyHit) {

                        mass -= 0.1 * Math.random();
                        carbon -= 0.0005 * Math.random();
                        impurities -= 0.025 * Math.random();
                        temp += 0.1 * Math.random();

                        if (mass <= 0) itemStack.setCount(0);
                        if (carbon <= 0.001) carbon = 0.001;
                        if (impurities < 0) impurities = 0;

                        if (Math.random() < 0.4) {
                            if (shape[1] > 1) {
                                shape[0] += 1;
                                shape[1] -= 1;

                            }
                            player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("block.anvil.use")), 1, 2);
                        } else {
                            player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("block.anvil.place")), 1, 1);
                        }

                        if (held.getItemDamage() > 1) {
                            held.damageItem(2, player);
                        } else {
                            held.damageItem(1, player);
                        }

                        player.getCooldownTracker().setCooldown(held.getItem(), 20);

                    } else {

                        mass -= 0.05 * Math.random();
                        carbon -= 0.0005 * Math.random();
                        impurities -= 0.01 * Math.random();
                        temp += 0.05 * Math.random();

                        if (mass <= 0) itemStack.setCount(0);
                        if (carbon <= 0.0001) carbon = 0.0001;
                        if (impurities < 0) impurities = 0;

                        if (Math.random() < 0.2) {
                            if (shape[0] > 1) {
                                shape[1] += 1;
                                shape[0] -= 1;
                            }
                            player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("block.anvil.use")), 1, 2);
                        } else {
                            player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("block.anvil.land")), 1, 1);
                        }

                        held.damageItem(1, player);

                        player.getCooldownTracker().setCooldown(held.getItem(), 10);

                    }

                Shagecraft.ITEMS.iron_cluster.setMass(itemStack, mass);
                Shagecraft.ITEMS.iron_cluster.setCarbon(itemStack, carbon);
                Shagecraft.ITEMS.iron_cluster.setImpurities(itemStack, impurities);
                Shagecraft.ITEMS.iron_cluster.setTemp(itemStack, temp);
                Shagecraft.ITEMS.iron_cluster.setShape(itemStack, shape);

            }

            }
        }
    }


}