package rebelkeithy.mods.metallurgy.core.metalsets;

import static rebelkeithy.mods.metallurgy.api.OreType.ALLOY;
import static rebelkeithy.mods.metallurgy.api.OreType.CATALYST;
import static rebelkeithy.mods.metallurgy.api.OreType.DROP;
import static rebelkeithy.mods.metallurgy.api.OreType.ORE;
import static rebelkeithy.mods.metallurgy.api.OreType.RESPAWN;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import rebelkeithy.mods.metablock.MetaBlock;
import rebelkeithy.mods.metablock.SubBlock;
import rebelkeithy.mods.metallurgy.api.IOreInfo;
import rebelkeithy.mods.metallurgy.api.OreType;
import rebelkeithy.mods.metallurgy.core.MetalInfoDatabase;
import rebelkeithy.mods.metallurgy.machines.abstractor.AbstractorRecipes;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class OreInfo implements IOreInfo, IWorldGenerator
{
	protected String setName;
	protected String name;
	protected CreativeTabs tab;
	protected OreType type;
	public int oreID;
	public int oreMeta;
	protected int blockID;
	protected int blockMeta;
	protected int brickID;
	protected int brickMeta;
	protected int itemIDs;
	
	protected String dropName;
	protected int dropMin;
	protected int dropMax;
	
	protected String[] alloyRecipe;
	protected int abstractorXP;
	protected int blockLvl;
	
	protected int pickLvl;
	protected int toolDura;
	protected int toolDamage;
	protected int toolSpeed;
	protected int toolEnchant;
	
	protected int helmetArmor;
	protected int chestArmor;
	protected int legsArmor;
	protected int bootsArmor;
	protected int armorDura;
	
	protected int dungeonLootChance;
	protected int dungeonLootAmount;
	protected int veinCount;
	protected int oreCount;
	protected int minHeight;
	protected int maxHeight;
	protected int veinChance;
	protected int veinDensity;
	protected String[] diminsions;
	
	
	public SubBlock ore;
	public SubBlock block;
	public SubBlock brick;
	
	public Item dust;
	public Item ingot;
	
	public Item pickaxe;
	public Item shovel;
	public Item axe;
	public Item hoe;
	public ItemMetallurgySword sword;
	
	public Item helmet;
	public Item chest;
	public Item legs;
	public Item boots;
	
	public OreInfo(Map<String, String> info, CreativeTabs tab)
	{
		setName = info.get("Metal Set");
		name = info.get("Name");
		this.tab = tab;
		System.out.println("reading " + name);
		if(info.get("Type").equals("Ore"))
			type = ORE;
		else if(info.get("Type").equals("Catalyst"))
			type = CATALYST;
		else if(info.get("Type").equals("Alloy"))
			type = ALLOY;
		else if(info.get("Type").equals("Respawn"))
			type = RESPAWN;
		else if(info.get("Type").equals("Drop"))
			type = DROP;
		
		alloyRecipe = info.get("Alloy Recipe").split("\" \"");
		System.out.println("alloy recipe: " + Arrays.toString(alloyRecipe));
		for(int n = 0; n < alloyRecipe.length; n++)
			alloyRecipe[n] = "dust" + alloyRecipe[n].replace("\"", "");
		
		if(type.generates())
		{
			oreID = Integer.parseInt(info.get("Ore ID").split(":")[0]);
			oreMeta = Integer.parseInt(info.get("Ore ID").split(":")[1]);
		}
		if(!info.get("Block ID").equals("0"))
		{
			blockID = Integer.parseInt(info.get("Block ID").split(":")[0]);
			blockMeta = Integer.parseInt(info.get("Block ID").split(":")[1]);
		}
		if(!info.get("Brick ID").equals("0"))
		{
			brickID = Integer.parseInt(info.get("Brick ID").split(":")[0]);
			brickMeta = Integer.parseInt(info.get("Brick ID").split(":")[1]);
		}
		
		if(type == DROP)
		{
			dropName = info.get("Drops").replace("\"", "");
			if(info.get("Drop Amount").contains("-"))
			{
				dropMin = Integer.parseInt(info.get("Drop Amount").split("-")[0]);
				dropMax = Integer.parseInt(info.get("Drop Amount").split("-")[1]);
			} else {
				dropMin = Integer.parseInt(info.get("Drop Amount"));
				dropMax = dropMin;
			}
		}
				
		itemIDs = Integer.parseInt(info.get("Item IDs"));
		
		abstractorXP = Integer.parseInt(info.get("Abstractor XP"));
		blockLvl = Integer.parseInt(info.get("Block lvl"));
		System.out.println("Block level default: " + info.get("Block lvl"));
		System.out.println("Block level set to : " + blockLvl);
		
		if(type != CATALYST && type != DROP)
		{
			pickLvl = Integer.parseInt(info.get("Pick lvl"));
			toolDura = Integer.parseInt(info.get("Durability"));
			toolDamage = Integer.parseInt(info.get("Damage"));
			toolSpeed = Integer.parseInt(info.get("Speed"));
			toolEnchant = Integer.parseInt(info.get("Enchantability"));
			
			helmetArmor = Integer.parseInt(info.get("Helmet Armor"));
			chestArmor = Integer.parseInt(info.get("Chestplate Armor"));
			legsArmor = Integer.parseInt(info.get("Leggings Armor"));
			bootsArmor = Integer.parseInt(info.get("Boots Armor"));
			armorDura = Integer.parseInt(info.get("Durability Multiplier"));
		}
		
		dungeonLootChance = Integer.parseInt(info.get("Dungeon Loot Chance"));
		dungeonLootAmount = Integer.parseInt(info.get("Dungeon Loot Amount"));
		
		if(type.generates())
		{
			veinCount = Integer.parseInt(info.get("Veins Per Chunk"));
			oreCount = Integer.parseInt(info.get("Ores Per Vein"));
			minHeight = Integer.parseInt(info.get("Min Level"));
			maxHeight = Integer.parseInt(info.get("Max Level"));
			veinChance = Integer.parseInt(info.get("Vein Chance Per Chunk"));
			veinDensity = Integer.parseInt(info.get("Vein Density"));
			diminsions = info.get("Diminsions").split(" ");
		}
		
	}

	public void initConfig(Configuration config) 
	{
		if(!type.equals(RESPAWN))
		{
			String id;
			if((type == ORE || type == CATALYST || type == DROP) && oreID != 0)
			{
				id = config.get(name + ".IDs", "Ore", oreID + ":" + oreMeta).getString();
				oreID = Integer.parseInt(id.split(":")[0]);
				oreMeta = Integer.parseInt(id.split(":")[1]);
			}
			if(type != DROP && blockID != 0)
			{
				id = config.get(name + ".IDs", "Block", blockID + ":" + blockMeta).getString();
				blockID = Integer.parseInt(id.split(":")[0]);
				blockMeta = Integer.parseInt(id.split(":")[1]);
			}
			if(type != DROP && brickID != 0)
			{
				id = config.get(name + ".IDs", "Brick", brickID + ":" + brickMeta).getString();
				brickID = Integer.parseInt(id.split(":")[0]);
				brickMeta = Integer.parseInt(id.split(":")[1]);
			}
			
			if(type != DROP)
			{
				itemIDs = config.get(name + ".IDs", "Item IDs (reserves 50)", itemIDs).getInt();
			
				abstractorXP = config.get(name + ".misc", "abstractor xp", abstractorXP).getInt();
			}
			
			blockLvl = config.get(name + ".misc", "Block Hardness Level", blockLvl).getInt();
			
			if(type != CATALYST && type != DROP)
			{
				pickLvl = config.get(name + ".Tool Info", "Pick Level", pickLvl).getInt();
				toolDura = config.get(name + ".Tool Info", "Durability", toolDura).getInt();
				toolDamage = config.get(name + ".Tool Info", "Damage", toolDamage).getInt();
				toolSpeed = config.get(name + ".Tool Info", "Speed", toolSpeed).getInt();
				toolEnchant = config.get(name + ".Tool Info", "Enchantability", toolEnchant).getInt();
				
				helmetArmor = config.get(name + ".Armor Info", "Helmet Armor", helmetArmor).getInt();
				chestArmor = config.get(name + ".Armor Info", "Chestplate Armor", chestArmor).getInt();
				legsArmor = config.get(name + ".Armor Info", "Leggings Armor", legsArmor).getInt();
				bootsArmor = config.get(name + ".Armor Info", "Boots Armor", bootsArmor).getInt();
				armorDura = config.get(name + ".Armor Info", "Durability Multiplier", armorDura).getInt();
			}
			
			if(type != DROP)
			{
				dungeonLootChance = config.get(name + ".World Gen", "Dungeon Loot Chance", dungeonLootChance).getInt();
				dungeonLootAmount = config.get(name + ".World Gen", "Dungeon Loot Amount", dungeonLootAmount).getInt();
			}
		}
		if(type.generates())
		{
			veinCount = config.get(name + ".World Gen", "Veins Per Chunk", veinCount).getInt();
			oreCount = config.get(name + ".World Gen", "Vein Size", oreCount).getInt();
			minHeight = config.get(name + ".World Gen", "Min Height", minHeight).getInt();
			maxHeight = config.get(name + ".World Gen", "Max Height", maxHeight).getInt();
			veinChance = config.get(name + ".World Gen", "Vein Chance", veinChance).getInt();
			veinDensity = config.get(name + ".World Gen", "Vein Density", veinDensity).getInt();
			
			String dimCombined = "";
			if(diminsions.length > 0)
			{
				dimCombined = diminsions[0];
				for(int n = 1; n < diminsions.length; n++)
					dimCombined += " " + diminsions[n];
			}
			diminsions = config.get(name + ".World Gen", "Diminsions", dimCombined).getString().split(" ");
		}
	}
	
	public void init()
	{
		System.out.println("Initializeing Ore " + name);
		if(!type.equals(RESPAWN))
		{
			if(type.generates() && oreID != 0)
			{
				ore = new SubBlock(oreID, oreMeta, "Metallurgy:" + setName + "/" + name + "Ore").setUnlocalizedName(setName + oreID).setCreativeTab(tab);
				if(type == DROP)
				{
					System.out.println("getting block drop " + dropName);
					ore.setBlockDrops(MetalInfoDatabase.getItem(dropName), dropMin, dropMax);
				}
			}
			if(type != DROP && blockID != 0)
			{
				block = new SubBlock(blockID, blockMeta, "Metallurgy:" + setName + "/" + name + "Block").setUnlocalizedName(setName + blockID).setCreativeTab(tab);
			}
			if(type != DROP && brickID != 0)
			{
				brick = new SubBlock(brickID, brickMeta, "Metallurgy:" + setName + "/" + name + "Brick").setUnlocalizedName(setName + brickID).setCreativeTab(tab);
			}
			if(type != DROP)
			{
				dust = new Item(itemIDs).setUnlocalizedName("Metallurgy:" + setName + "/" + name + "Dust").setCreativeTab(tab);
				ingot = new ItemMetallurgy(itemIDs+1).setSmeltinExperience(abstractorXP/3f).setUnlocalizedName("Metallurgy:" + setName + "/" + name + "Ingot").setCreativeTab(tab);
				AbstractorRecipes.addEssence(ingot.itemID, 0, abstractorXP);
			}
			
			if(type != CATALYST && type != DROP)
			{
				EnumToolMaterial toolEnum = EnumHelper.addToolMaterial(name, pickLvl, toolDura, toolSpeed, toolDamage, toolEnchant);
				toolEnum.customCraftingMaterial = ingot;
				System.out.println(name.toUpperCase() + "TOOL SPEED = " + toolSpeed);
				pickaxe = new ItemPickaxe(itemIDs + 2, toolEnum).setUnlocalizedName("Metallurgy:" + setName + "/" + name + "Pick").setCreativeTab(tab);
				shovel = new ItemSpade(itemIDs + 3, toolEnum).setUnlocalizedName("Metallurgy:" + setName + "/" + name + "Shovel").setCreativeTab(tab);
				axe = new ItemAxe(itemIDs + 4, toolEnum).setUnlocalizedName("Metallurgy:" + setName + "/" + name + "Axe").setCreativeTab(tab);
				hoe = new ItemHoe(itemIDs + 5, toolEnum).setUnlocalizedName("Metallurgy:" + setName + "/" + name + "Hoe").setCreativeTab(tab);
				sword = (ItemMetallurgySword) new ItemMetallurgySword(itemIDs + 6, toolEnum).setUnlocalizedName("Metallurgy:" + setName + "/" + name + "Sword").setCreativeTab(tab);
				
				EnumArmorMaterial armorEnum = EnumHelper.addArmorMaterial(name, armorDura, new int[] {helmetArmor, chestArmor, legsArmor, bootsArmor}, toolEnchant);
				armorEnum.customCraftingMaterial = ingot;
				String armorTexture = name;
				armorTexture = armorTexture.replaceAll("\\s","").toLowerCase();
				helmet = new ItemMetallurgyArmor(itemIDs + 7, armorEnum, 0, 0).setTextureFile(armorTexture + "_1").setUnlocalizedName("Metallurgy:" + setName + "/" + name + "Helmet").setCreativeTab(tab);
				chest = new ItemMetallurgyArmor(itemIDs + 8, armorEnum, 1, 1).setTextureFile(armorTexture + "_1").setUnlocalizedName("Metallurgy:" + setName + "/" + name + "Chest").setCreativeTab(tab);
				legs = new ItemMetallurgyArmor(itemIDs + 9, armorEnum, 2, 2).setTextureFile(armorTexture + "_2").setUnlocalizedName("Metallurgy:" + setName + "/" + name + "Legs").setCreativeTab(tab);
				boots = new ItemMetallurgyArmor(itemIDs + 10, armorEnum, 3, 3).setTextureFile(armorTexture + "_1").setUnlocalizedName("Metallurgy:" + setName + "/" + name + "Boots").setCreativeTab(tab);
			}
		}
		
		if(type.generates())
		{
			GameRegistry.registerWorldGenerator(this);
		}
	}

	public void load()
	{
		if(!type.equals(RESPAWN))
		{
			if(oreID != 0)
				MetaBlock.registerID(oreID);
			if(brickID != 0)
				MetaBlock.registerID(brickID);
			if(blockID != 0)
				MetaBlock.registerID(blockID);

			setLevels();
			if(type != DROP)
			{
				addRecipes();
			}
			registerMetal();
		}
	}
	
	public void setLevels()
	{
		MinecraftForge.setToolClass(pickaxe, "pickaxe", pickLvl);
		if(ore != null)
		{
			MinecraftForge.setBlockHarvestLevel(ore.getBlock(), oreMeta, "pickaxe", blockLvl);
			ore.setHardness(2F).setResistance(1F);
		}
		if(block != null)
		{
			MinecraftForge.setBlockHarvestLevel(block.getBlock(), blockMeta, "pickaxe", blockLvl);
		}
		if(brick != null)
		{
			MinecraftForge.setBlockHarvestLevel(brick.getBlock(), brickMeta, "pickaxe", blockLvl);
		}
	}
	
	public void addRecipes()
	{
		if(type.generates() && ore != null)
		{
			FurnaceRecipes.smelting().addSmelting(oreID, oreMeta, new ItemStack(ingot), 0);	
		}
		
		FurnaceRecipes.smelting().addSmelting(dust.itemID, 0, new ItemStack(ingot), 0);

		ShapedOreRecipe recipe;
		if(block != null)
		{
			recipe = new ShapedOreRecipe(new ItemStack(blockID, 1, blockMeta), "XXX", "XXX", "XXX", 'X', "ingot" + name);
			GameRegistry.addRecipe(recipe);
			GameRegistry.addShapelessRecipe(new ItemStack(ingot, 9), new ItemStack(blockID, 1, blockMeta));
		}
		if(brick != null)
		{
			recipe = new ShapedOreRecipe(new ItemStack(brickID, 4, brickMeta), "XX", "XX", 'X', "ingot" + name);
			GameRegistry.addRecipe(recipe);
			GameRegistry.addShapelessRecipe(new ItemStack(ingot, 1), new ItemStack(brickID, 1, brickMeta));
		}
		
		if(type != CATALYST)
		{
			recipe = new ShapedOreRecipe(new ItemStack(pickaxe), "XXX", " S ", " S ", 'X', "ingot" + name, 'S', Item.stick);
			GameRegistry.addRecipe(recipe);
			recipe = new ShapedOreRecipe(new ItemStack(shovel), "X", "S", "S", 'X', "ingot" + name, 'S', Item.stick);
			GameRegistry.addRecipe(recipe);
			recipe = new ShapedOreRecipe(new ItemStack(axe), "XX", "SX", "S ", 'X', "ingot" + name, 'S', Item.stick);
			GameRegistry.addRecipe(recipe);
			recipe = new ShapedOreRecipe(new ItemStack(hoe), "XX", "S ", "S ", 'X', "ingot" + name, 'S', Item.stick);
			GameRegistry.addRecipe(recipe);
			recipe = new ShapedOreRecipe(new ItemStack(sword), "X", "X", "S", 'X', "ingot" + name, 'S', Item.stick);
			GameRegistry.addRecipe(recipe);
			
			recipe = new ShapedOreRecipe(new ItemStack(helmet), "XXX", "X X", 'X', "ingot" + name);
			GameRegistry.addRecipe(recipe);
			recipe = new ShapedOreRecipe(new ItemStack(chest), "X X", "XXX", "XXX", 'X', "ingot" + name);
			GameRegistry.addRecipe(recipe);
			recipe = new ShapedOreRecipe(new ItemStack(legs), "XXX", "X X", "X X", 'X', "ingot" + name);
			GameRegistry.addRecipe(recipe);
			recipe = new ShapedOreRecipe(new ItemStack(boots), "X X", "X X", 'X', "ingot" + name);
			GameRegistry.addRecipe(recipe);
			
			recipe = new ShapedOreRecipe(new ItemStack(Item.bucketEmpty), "X X", " X ", 'X', "ingot" + name);
			GameRegistry.addRecipe(recipe);
			recipe = new ShapedOreRecipe(new ItemStack(Item.shears), "X ", " X", 'X', "ingot" + name);
			GameRegistry.addRecipe(recipe);
		}
		
		if(type == ALLOY)
		{
			System.out.println("Adding alloy recipe " + alloyRecipe[0] + " + " + alloyRecipe[1] + " for " + name);
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(dust, 2), alloyRecipe));
		}
	}
	
	public void registerMetal()
	{
		if(ore != null)
		{
			OreDictionary.registerOre("ore" + name, new ItemStack(oreID, 1, oreMeta));
		}
		if(block != null)
			OreDictionary.registerOre("block" + name, new ItemStack(blockID, 1, blockMeta));
		if(type != DROP)
		{
			OreDictionary.registerOre("ingot" + name, ingot);
			OreDictionary.registerOre("dust" + name, dust);
		}
	}

	public void registerNames() {
		if(type == RESPAWN)
			return;
		
		if(type.generates() && ore != null)
		{
			LanguageRegistry.addName(new ItemStack(oreID, 1, oreMeta), name + " Ore");
		}
		if(block != null)
			LanguageRegistry.addName(new ItemStack(blockID, 1, blockMeta), name + " Block");
		if(brick != null)
			LanguageRegistry.addName(new ItemStack(brickID, 1, brickMeta), name + " Brick");
		
		if(type != DROP)
		{
			LanguageRegistry.addName(dust, name + " Dust");
			LanguageRegistry.addName(ingot, name + " Ingot");
		}
		
		if(type != CATALYST && type != DROP)
		{
			LanguageRegistry.addName(pickaxe, name + " Pickaxe");
			LanguageRegistry.addName(shovel, name + " Shovel");
			LanguageRegistry.addName(axe, name + " Axe");
			LanguageRegistry.addName(hoe, name + " Hoe");
			LanguageRegistry.addName(sword, name + " Sword");
			
			LanguageRegistry.addName(helmet, name + " Helmet");
			LanguageRegistry.addName(chest, name + " Chestplate");
			LanguageRegistry.addName(legs, name + " Legs");
			LanguageRegistry.addName(boots, name + " Boots");
		}
	}
	
	private boolean spawnsInDim(int dim)
	{
		for(String string : diminsions)
		{
			//System.out.println("checking dim '" + string + "' against" + dim);
			if(string.contains("-") && !string.startsWith("-"))
			{
				int min = Integer.parseInt(string.split("-")[0]);
				int max = Integer.parseInt(string.split("-")[1]);
				if(min > max)
				{
					int temp = min;
					min = max;
					max = temp;
				}
				if(dim >= min && dim <= max)
					return true;
			} else {
				int check = Integer.parseInt(string);
				if(dim == check)
					return true;
			}
		}
		return false;
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	{
		if(!type.generates() || (ore == null && type != RESPAWN) || !spawnsInDim(world.provider.dimensionId))
			return;
		
		for(int n = 0; n < veinCount; n++)
		{
			int randPosX = chunkX*16 + random.nextInt(16);
			int randPosY = random.nextInt(maxHeight - minHeight) + minHeight;
			int randPosZ = chunkZ*16 + random.nextInt(16);
			
			//new WorldGenMinable(oreID, oreMeta, oreCount).generate(world, random, randPosX, randPosY, randPosZ);
			new MetallurgyWorldGenMinable(oreID, oreMeta, oreCount, veinDensity, Block.stone.blockID, 0).generate(world, random, randPosX, randPosY, randPosZ);
			//new WorldGenMinable(oreID, oreMeta, 40).generate(world, random, randPosX, randPosY, randPosZ);
		}
	}

	@Override
	public String getName() 
	{
		return name;
	}

	@Override
	public ItemStack getOre() 
	{
		return new ItemStack(oreID, 1, oreMeta);
	}

	@Override
	public ItemStack getBlock() 
	{
		return new ItemStack(blockID, 1, blockMeta);
	}

	@Override
	public ItemStack getBrick() 
	{
		return new ItemStack(brickID, 1, brickMeta);
	}

	@Override
	public ItemStack getDust() 
	{
		return new ItemStack(dust);
	}

	@Override
	public ItemStack getIngot() 
	{
		return new ItemStack(ingot);
	}

	@Override
	public String[] getAlloyRecipe() 
	{
		return alloyRecipe;
	}

	@Override
	public OreType getType() 
	{
		return type;
	}
}
