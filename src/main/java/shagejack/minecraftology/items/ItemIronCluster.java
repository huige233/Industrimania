package shagejack.minecraftology.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.items.includes.MCLBaseItem;
import shagejack.minecraftology.util.MCLStringHelper;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

public class ItemIronCluster extends MCLBaseItem {
    public ItemIronCluster(String name) {
        super(name);
    }

    @Override
    public boolean hasDetails(ItemStack stack) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
        super.addDetails(itemstack, player, worldIn, infos);
        infos.add("\u8d28\u91cf(Mass): " + getMass(itemstack)  + "g");
    }

    public double getMass(ItemStack item) {
        if (item.hasTagCompound()) {
            return item.getTagCompound().getDouble("mass");
        }
        return 0;
    }

    public void setMass(ItemStack itemStack, double mass) {
        TagCompountCheck(itemStack);
        itemStack.getTagCompound().setDouble("mass", mass);
    }

}
