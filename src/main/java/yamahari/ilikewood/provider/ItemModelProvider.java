package yamahari.ilikewood.provider;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import yamahari.ilikewood.registry.WoodenBlocks;
import yamahari.ilikewood.util.Constants;
import yamahari.ilikewood.util.IWooden;
import yamahari.ilikewood.util.Util;
import yamahari.ilikewood.util.WoodenObjectType;

public final class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(final DataGenerator generator, final ExistingFileHelper helper) {
        super(generator, Constants.MOD_ID, helper);
    }

    private void blockItem(final Block block, final String path) {
        final String type = ((IWooden) block).getWoodType().toString();
        this.getBuilder(block.getRegistryName().getPath())
                .parent(new ModelFile.UncheckedModelFile(modLoc(Util.toPath(BLOCK_FOLDER, path, type))));
    }

    @Override
    protected void registerModels() {
        WoodenBlocks.getBlocks(WoodenObjectType.PANELS).forEach(block -> this.blockItem(block, WoodenObjectType.PANELS.toString()));
        WoodenBlocks.getBlocks(WoodenObjectType.STAIRS).forEach(block -> this.blockItem(block, Util.toPath(WoodenObjectType.PANELS.toString(), WoodenObjectType.STAIRS.toString())));
        WoodenBlocks.getBlocks(WoodenObjectType.SLAB).forEach(block -> this.blockItem(block, Util.toPath(WoodenObjectType.PANELS.toString(), WoodenObjectType.SLAB.toString())));
        WoodenBlocks.getBlocks(WoodenObjectType.BARREL).forEach(block -> this.blockItem(block, WoodenObjectType.BARREL.toString()));
        WoodenBlocks.getBlocks(WoodenObjectType.BOOKSHELF).forEach(block -> this.blockItem(block, WoodenObjectType.BOOKSHELF.toString()));
        WoodenBlocks.getBlocks(WoodenObjectType.CHEST).forEach(block -> {
            final String type = ((IWooden) block).getWoodType().toString();
            this.getBuilder(block.getRegistryName().getPath())
                    .parent(new ModelFile.UncheckedModelFile(mcLoc(Util.toPath("builtin", "entity"))))
                    .texture("particle", mcLoc(Util.toPath(BLOCK_FOLDER, type + "_planks")))
                    .transforms()
                    .transform(ModelBuilder.Perspective.GUI)
                    .rotation(30, 45, 0)
                    .scale(0.625F)
                    .end()
                    .transform(ModelBuilder.Perspective.GROUND)
                    .translation(0, 3, 0)
                    .scale(0.25F)
                    .end()
                    .transform(ModelBuilder.Perspective.HEAD)
                    .rotation(0, 180, 0)
                    .scale(1.f)
                    .end()
                    .transform(ModelBuilder.Perspective.FIXED)
                    .rotation(0, 180, 0)
                    .scale(0.5F)
                    .end()
                    .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT)
                    .rotation(75, 315, 0)
                    .translation(0.F, 2.5F, 0.F)
                    .scale(0.375F)
                    .end()
                    .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT)
                    .rotation(0, 315, 0)
                    .scale(0.4F)
                    .end();
        });
        WoodenBlocks.getBlocks(WoodenObjectType.COMPOSTER).forEach(block -> this.blockItem(block, WoodenObjectType.COMPOSTER.toString()));
    }

    @Override
    public String getName() {
        return "I Like Wood - Item Models";
    }
}
