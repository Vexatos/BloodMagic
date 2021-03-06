package WayofTime.alchemicalWizardry.common.rituals;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;

public class RitualEffectSoulBound extends RitualEffect
{
    @Override
    public void performEffect(TEMasterStone ritualStone)
    {
        String owner = ritualStone.getOwner();
        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) worldSave.loadItemData(LifeEssenceNetwork.class, owner);

        if (data == null)
        {
            data = new LifeEssenceNetwork(owner);
            worldSave.setItemData(owner, data);
        }

        int currentEssence = data.currentEssence;
        World world = ritualStone.getWorldObj();
        int x = ritualStone.xCoord;
        int y = ritualStone.yCoord;
        int z = ritualStone.zCoord;

        if (currentEssence < this.getCostPerRefresh())
        {
            EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            if (ritualStone.getVar1() == 0)
            {
                int d0 = 0;
                AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double) x, (double) y + 1, (double) z, (double) (x + 1), (double) (y + 2), (double) (z + 1)).expand(d0, d0, d0);
                List list = world.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);
                Iterator iterator = list.iterator();
                EntityItem item;

                while (iterator.hasNext())
                {
                    item = (EntityItem) iterator.next();
//                double xDif = item.posX - (xCoord+0.5);
//                double yDif = item.posY - (yCoord+1);
//                double zDif = item.posZ - (zCoord+0.5);
                    ItemStack itemStack = item.getEntityItem();

                    if (itemStack == null)
                    {
                        continue;
                    }

                    ItemStack itemGoggles = null;

                    if (AlchemicalWizardry.isThaumcraftLoaded)
                    {
                        //itemGoggles = ItemApi.getItem("itemGoggles", 0);
                    }

                    if (itemStack.getItem() == ModItems.apprenticeBloodOrb)
                    {
                        ritualStone.setVar1(Item.getIdFromItem(ModItems.energyBlaster));
                        world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z));
                        ritualStone.setCooldown(ritualStone.getCooldown() - 1);
                        item.setDead();
                        return;
                    } else if (itemStack.getItem() == Items.diamond_sword)
                    {
                        ritualStone.setVar1(Item.getIdFromItem(ModItems.energySword));
                        world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z));
                        ritualStone.setCooldown(ritualStone.getCooldown() - 1);
                        item.setDead();
                        return;
                    } else if (itemStack.getItem() == Items.diamond_pickaxe)
                    {
                        ritualStone.setVar1(Item.getIdFromItem(ModItems.boundPickaxe));
                        world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z));
                        ritualStone.setCooldown(ritualStone.getCooldown() - 1);
                        item.setDead();
                        return;
                    } else if (itemStack.getItem() == Items.diamond_axe)
                    {
                        ritualStone.setVar1(Item.getIdFromItem(ModItems.boundAxe));
                        world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z));
                        ritualStone.setCooldown(ritualStone.getCooldown() - 1);
                        item.setDead();
                        return;
                    } else if (itemStack.getItem() == Items.diamond_shovel)
                    {
                        ritualStone.setVar1(Item.getIdFromItem(ModItems.boundShovel));
                        world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z));
                        ritualStone.setCooldown(ritualStone.getCooldown() - 1);
                        item.setDead();
                        return;
                    } 
//                    else if (itemGoggles != null && itemGoggles.isItemEqual(itemStack))
//                    {
//                        ritualStone.setVar1(Item.getIdFromItem(ModItems.sanguineHelmet));
//                        world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z));
//                        ritualStone.setCooldown(ritualStone.getCooldown() - 1);
//                        item.setDead();
//                        return;
//                    }

                    if (world.rand.nextInt(10) == 0)
                    {
                        SpellHelper.sendIndexedParticleToAllAround(world, x, y, z, 20, world.provider.dimensionId, 1, x, y, z);
                    }
                }

                data.currentEssence = currentEssence - this.getCostPerRefresh();
                data.markDirty();
            } else
            {
                ritualStone.setCooldown(ritualStone.getCooldown() - 1);

                if (world.rand.nextInt(20) == 0)
                {
                    int lightningPoint = world.rand.nextInt(8);

                    switch (lightningPoint)
                    {
                        case 0:
                            world.addWeatherEffect(new EntityLightningBolt(world, x + 4, y + 3, z + 0));
                            break;

                        case 1:
                            world.addWeatherEffect(new EntityLightningBolt(world, x - 4, y + 3, z + 0));
                            break;

                        case 2:
                            world.addWeatherEffect(new EntityLightningBolt(world, x + 0, y + 3, z + 4));
                            break;

                        case 3:
                            world.addWeatherEffect(new EntityLightningBolt(world, x + 0, y + 3, z - 4));
                            break;

                        case 4:
                            world.addWeatherEffect(new EntityLightningBolt(world, x + 3, y + 3, z + 3));
                            break;

                        case 5:
                            world.addWeatherEffect(new EntityLightningBolt(world, x - 3, y + 3, z + 3));
                            break;

                        case 6:
                            world.addWeatherEffect(new EntityLightningBolt(world, x + 3, y + 3, z - 3));
                            break;

                        case 7:
                            world.addWeatherEffect(new EntityLightningBolt(world, x - 3, y + 3, z - 3));
                            break;
                    }
                }

                if (ritualStone.getCooldown() <= 0)
                {
                    ItemStack spawnedItem = new ItemStack(Item.getItemById(ritualStone.getVar1()), 1, 0);

                    if (spawnedItem != null)
                    {
                        EntityItem newItem = new EntityItem(world, x + 0.5, y + 1, z + 0.5, spawnedItem);
                        world.spawnEntityInWorld(newItem);
                    }

                    ritualStone.setActive(false);
                }
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 0;
    }

    @Override
    public int getInitialCooldown()
    {
        return 200;
    }
}
