package yamahari.ilikewood.provider;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import yamahari.ilikewood.registry.WoodenBlocks;
import yamahari.ilikewood.util.Constants;
import yamahari.ilikewood.util.IWooden;
import yamahari.ilikewood.util.Util;
import yamahari.ilikewood.util.WoodenObjectType;

import java.util.stream.IntStream;

public final class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    public BlockStateProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Constants.MOD_ID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        WoodenBlocks.getBlocks(WoodenObjectType.PANELS).forEach(block -> {
            final String type = ((IWooden) block).getWoodType().toString();
            final ModelFile model = this.models().singleTexture(
                    Util.toPath(ModelProvider.BLOCK_FOLDER, WoodenObjectType.PANELS.toString(), type),
                    modLoc(Util.toPath(ModelProvider.BLOCK_FOLDER, WoodenObjectType.PANELS.toString(), "template")),
                    "planks",
                    mcLoc(Util.toPath(ModelProvider.BLOCK_FOLDER, type + "_planks")));

            this.simpleBlock(block, model);
        });
        WoodenBlocks.getBlocks(WoodenObjectType.STAIRS).forEach(block -> {
            final String type = ((IWooden) block).getWoodType().toString();
            final String path = Util.toPath(ModelProvider.BLOCK_FOLDER, WoodenObjectType.PANELS.toString(), WoodenObjectType.STAIRS.toString());
            final ModelFile stairs = this.models().singleTexture(
                    Util.toPath(path, type),
                    modLoc(Util.toPath(path, "template")),
                    "planks",
                    mcLoc(Util.toPath(ModelProvider.BLOCK_FOLDER, type + "_planks")));

            final ModelFile stairsInner = this.models().singleTexture(
                    Util.toPath(path, "inner", type),
                    modLoc(Util.toPath(path, "inner", "template")),
                    "planks",
                    mcLoc(Util.toPath(ModelProvider.BLOCK_FOLDER, type + "_planks")));

            final ModelFile stairsOuter = this.models().singleTexture(
                    Util.toPath(path, "outer", type),
                    modLoc(Util.toPath(path, "outer", "template")),
                    "planks",
                    mcLoc(Util.toPath(ModelProvider.BLOCK_FOLDER, type + "_planks")));
            this.stairsBlock((StairsBlock) block, stairs, stairsInner, stairsOuter);
        });
        WoodenBlocks.getBlocks(WoodenObjectType.SLAB).forEach(block -> {
            final String type = ((IWooden) block).getWoodType().toString();
            final String path = Util.toPath(ModelProvider.BLOCK_FOLDER, WoodenObjectType.PANELS.toString(), WoodenObjectType.SLAB.toString());
            final ModelFile slabBottom = this.models().singleTexture(
                    Util.toPath(path, type),
                    modLoc(Util.toPath(path, "template")),
                    "planks",
                    mcLoc(Util.toPath(ModelProvider.BLOCK_FOLDER, type + "_planks")));

            final ModelFile slabTop = this.models().singleTexture(
                    Util.toPath(path, "top", type),
                    modLoc(Util.toPath(path, "top", "template")),
                    "planks",
                    mcLoc(Util.toPath(ModelProvider.BLOCK_FOLDER, type + "_planks")));

            final ModelFile slabDouble = new ModelFile.UncheckedModelFile(modLoc(Util.toPath(ModelProvider.BLOCK_FOLDER, "panels", type)));

            this.slabBlock((SlabBlock) block, slabBottom, slabTop, slabDouble);
        });
        WoodenBlocks.getBlocks(WoodenObjectType.BARREL).forEach(block -> {
            final String path = Util.toPath(ModelProvider.BLOCK_FOLDER, WoodenObjectType.BARREL.toString(), "%s", ((IWooden) block).getWoodType().toString());
            this.directionalBlock(block,
                    state -> {
                        final boolean open = state.get(BarrelBlock.PROPERTY_OPEN);
                        return this.models().cubeBottomTop(
                                String.format(path, (open ? "open" : "")),
                                modLoc(String.format(path, "side")),
                                modLoc(String.format(path, "bottom")),
                                modLoc(String.format(path, "top" + (open ? "/open" : "")))
                        );
                    }
            );
        });
        WoodenBlocks.getBlocks(WoodenObjectType.BOOKSHELF).forEach(block -> {
            final String type = ((IWooden) block).getWoodType().toString();
            final String path = Util.toPath(ModelProvider.BLOCK_FOLDER, WoodenObjectType.BOOKSHELF.toString(), type);
            this.simpleBlock(block, this.models().cubeColumn(
                    path, modLoc(path), mcLoc(Util.toPath(ModelProvider.BLOCK_FOLDER, type + "_planks"))));
        });
        WoodenBlocks.getBlocks(WoodenObjectType.CHEST).forEach(block -> {
            final String type = ((IWooden) block).getWoodType().toString();
            final String path = Util.toPath(ModelProvider.BLOCK_FOLDER, WoodenObjectType.CHEST.toString(), type);
            this.simpleBlock(block, this.models().getBuilder(path)
                    .texture("particle", mcLoc(Util.toPath(ModelProvider.BLOCK_FOLDER, type + "_planks"))));
        });
        WoodenBlocks.getBlocks(WoodenObjectType.COMPOSTER).forEach(block -> {
            final String type = ((IWooden) block).getWoodType().toString();
            final String path = Util.toPath(ModelProvider.BLOCK_FOLDER, WoodenObjectType.COMPOSTER.toString());
            final ModelFile template = this.models()
                    .withExistingParent(Util.toPath(path, type), modLoc(Util.toPath(path, "template")))
                    .texture("top", Util.toPath(path, "top", type))
                    .texture("side", Util.toPath(path, "side", type))
                    .texture("bottom", Util.toPath(path, "bottom", type));

            final MultiPartBlockStateBuilder builder = this.getMultipartBuilder(block)
                    .part()
                    .modelFile(template)
                    .addModel()
                    .end();

            IntStream.range(1, 9).forEach(level -> {
                final ModelFile content = new ModelFile.UncheckedModelFile(mcLoc(Util.toPath(ModelProvider.BLOCK_FOLDER, String.format("composter_contents%s", level != 8 ? level : "_ready"))));
                builder.part()
                        .modelFile(content)
                        .addModel()
                        .condition(ComposterBlock.field_220298_a, level)
                        .end();
            });
        });
    }

    @Override
    public String getName() {
        return "I Like Wood - Blockstates";
    }
}
