package WayofTime.alchemicalWizardry.common.items.sigil;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import java.util.List;

public class SigilOfTheFastMiner extends EnergyItems implements ArmourUpgrade
{
    @SideOnly(Side.CLIENT)
    private static Icon activeIcon;
    @SideOnly(Side.CLIENT)
    private static Icon passiveIcon;

    public SigilOfTheFastMiner(int id)
    {
        super(id);
        this.maxStackSize = 1;
        //setMaxDamage(100);
        setEnergyUsed(100);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Keep going and going and going...");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            if (par1ItemStack.stackTagCompound.getBoolean("isActive"))
            {
                par3List.add("Activated");
            } else
            {
                par3List.add("Deactivated");
            }

            par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SigilOfTheFastMiner");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:MiningSigil_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:MiningSigil_deactivated");
    }

    @Override
    public Icon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        if (stack.stackTagCompound == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.stackTagCompound;

        if (tag.getBoolean("isActive"))
        {
            return this.activeIcon;
        } else
        {
            return this.passiveIcon;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1)
    {
        if (par1 == 1)
        {
            return this.activeIcon;
        } else
        {
            return this.passiveIcon;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = par1ItemStack.stackTagCompound;
        tag.setBoolean("isActive", !(tag.getBoolean("isActive")));

        if (tag.getBoolean("isActive"))
        {
            par1ItemStack.setItemDamage(1);
            tag.setInteger("worldTimeDelay", (int) (par2World.getWorldTime() - 1) % 200);
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 2, 1, true));

            //Test with added health boost
            //par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.field_76444_x.id, 2400,99));
            if (!par3EntityPlayer.capabilities.isCreativeMode)
            {
                if (!EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
                {
                }
            }
        } else
        {
            par1ItemStack.setItemDamage(par1ItemStack.getMaxDamage());
        }

        return par1ItemStack;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (!(par3Entity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;

        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        if (par1ItemStack.stackTagCompound.getBoolean("isActive"))
        {
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 2, 1, true));
        }

        if (par2World.getWorldTime() % 200 == par1ItemStack.stackTagCompound.getInteger("worldTimeDelay") && par1ItemStack.stackTagCompound.getBoolean("isActive"))
        {
            //par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.field_76444_x.id, 2400,99));
            if (!par3EntityPlayer.capabilities.isCreativeMode)
            {
                if (!EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
                {
                }
            }
        }

        return;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack itemStack)
    {
        if (itemStack.stackTagCompound == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 3, 1, true));
    }

    @Override
    public boolean isUpgrade()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        // TODO Auto-generated method stub
        return 50;
    }
}
