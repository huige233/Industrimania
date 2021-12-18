package shagejack.shagecraft.registers.dataGen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import shagejack.shagecraft.ShageCraft;
import shagejack.shagecraft.util.Wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static shagejack.shagecraft.ShageCraft.MOD_ID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenHandle {


    private static final List<Consumer<ItemModelProvider>> itemModelTasks = new ArrayList();
    private static final ArrayList<Consumer<BlockModelProvider>> blockModelTasks = new ArrayList();
    private static Wrapper<ItemModelProvider> itemModelPro = new Wrapper();
    private static Wrapper<BlockModelProvider> blockModelPro = new Wrapper();
    public static Lazy<ExistingModelFile> itemGeneratedModel = () -> existingModel(itemModelPro.get(), "item/generated");
    public static Lazy<ExistingModelFile> itemHeldModel = () -> existingModel(itemModelPro.get(), "item/handheld");
    public static Lazy<UncheckedModelFile> blockBuiltinEntity = () -> uncheckedModel( "builtin/entity");
    public static Lazy<ExistingModelFile> blockCubeAll = () -> existingModel(itemModelPro.get(), "block/cube_all");
    public static Lazy<ExistingModelFile> blockCube = () -> existingModel(itemModelPro.get(), "block/cube");

    public static void addItemModelTask(Consumer<ItemModelProvider> task) {
        runOnDataGen(() -> () -> {
            itemModelTasks.add(task);
        });
    }

    public static void addBlockModelTask(Consumer<BlockModelProvider> task) {
        runOnDataGen(() -> () -> {
            blockModelTasks.add(task);
        });
    }

    public static void runOnDataGen(Supplier<DistExecutor.SafeRunnable> toRun) {
        if (FMLEnvironment.dist == Dist.CLIENT && ShageCraft.isDataGen) {
            toRun.get().run();
        }
    }

    static ExistingModelFile existingModel(ModelProvider<?> provider, String path) {
        return provider.getExistingFile(provider.mcLoc(path));
    }

    static UncheckedModelFile uncheckedModel(String path) {
        return new UncheckedModelFile(path);
    }


//    static ExistingModelFile ItemGeneratedModel(ModelProvider<?> provider) {
//        return existingModel(provider, "item/generated");
//    }
//
//    public static ExistingModelFile ItemHeldModel(ModelProvider<?> provider) {
//        return existingModel(provider, "item/handheld");
//    }
//
//    static UncheckedModelFile BlockBuiltinEntity(ModelProvider<?> provider) {
//        return new UncheckedModelFile("builtin/entity");
//    }
//
//    static ExistingModelFile BlockCubeAll(ModelProvider<?> provider) {
//        return existingModel(provider, "block/cube_all");
//    }
//
//    static ExistingModelFile BlockCube(ModelProvider<?> provider) {
//        return existingModel(provider, "block/cube");
//    }


    @SubscribeEvent
    public static void processDataGen(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        ItemModelProvider itemModelProvider = new ItemModelProvider(generator, MOD_ID, existingFileHelper) {
            @Override
            protected void registerModels() {
                itemModelTasks.forEach((task) -> task.accept(this));
            }
        };

        BlockModelProvider blockModelProvider = new BlockModelProvider(generator, MOD_ID, existingFileHelper) {
            @Override
            protected void registerModels() {
                blockModelTasks.forEach((task) -> task.accept(this));
            }
        };

//        LanguageProvider languageProvider = new LanguageProvider(generator,MOD_ID,) {
//            @Override
//            protected void addTranslations() {
//
//            }
//        };

        itemModelPro.set(() -> itemModelProvider);
        blockModelPro.set( () -> blockModelProvider);

        generator.addProvider(itemModelProvider);
        generator.addProvider(blockModelProvider);
//        generator.addProvider(languageProvider);
    }
}