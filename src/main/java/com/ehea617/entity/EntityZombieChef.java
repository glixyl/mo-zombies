package com.ehea617.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityZombieChef extends EntityMob
{
	public EntityZombieChef(World par1World)
	{
		super(par1World);
        this.getNavigator().setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.23D, false));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityVillager.class, 0.23D, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.23D));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 0.23D, false));
        this.tasks.addTask(7, new EntityAIWander(this, 0.23D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, false));
        this.setSize(0.6F, 1.8F);
	}
	
	public boolean isAIEnabled()
	{
		return true;
	}

    public void onLivingUpdate()
    {
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
        if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isChild())
        {
            float f = this.getBrightness(1.0F);

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)))
            {
                boolean flag = true;
                ItemStack itemstack = this.getEquipmentInSlot(4);

                if (itemstack != null)
                {
                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + this.rand.nextInt(2));

                        if (itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage())
                        {
                            this.renderBrokenItemStack(itemstack);
                            this.setCurrentItemOrArmor(4, (ItemStack)null);
                        }
                    }
                    flag = false;
                }
                if (flag)
                {
                    this.setFire(8);
                }
            }
        }
        super.onLivingUpdate();
    }
	
    protected String getLivingSound()
    {
        return "mob.zombie.say";
    }
    
    protected String getHurtSound()
    {
        return "mob.zombie.hurt";
    }
    
    protected String getDeathSound()
    {
        return "mob.zombie.death";
    }
    
    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
    {
        this.playSound("mob.zombie.step", 0.15F, 1.0F);
    }
    
    public ItemStack getHeldItem()
    {
		return new ItemStack(Items.iron_axe, 1);
    }
    
    private static final Item[] zombieChefDrops = new Item[] {Items.apple, Items.baked_potato, Items.beef, Items.bread, Items.cake, Items.carrot, Items.chicken, Items.cooked_beef, Items.cooked_chicken, Items.cooked_fished, Items.cooked_porkchop, Items.cookie, Items.egg, Items.fish, Items.melon, Items.mushroom_stew, Items.potato, Items.pumpkin_pie, Items.sugar};
    protected void dropFewItems(boolean par1, int par2)
    {
        int j = this.rand.nextInt(3) + 1;

        for (int k = 0; k < j; ++k)
        {
            int l = this.rand.nextInt(3);
            Item item = zombieChefDrops[this.rand.nextInt(zombieChefDrops.length)];

            if (par2 > 0)
            {
                l += this.rand.nextInt(par2 + 1);
            }

            for (int i1 = 0; i1 < l; ++i1)
            {
                this.dropItem(item, 1);
            }
        }
    }
    
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }
}
