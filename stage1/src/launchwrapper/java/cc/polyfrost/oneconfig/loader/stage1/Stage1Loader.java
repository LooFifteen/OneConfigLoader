package cc.polyfrost.oneconfig.loader.stage1;

import cc.polyfrost.oneconfig.loader.ILoader;
import cc.polyfrost.oneconfig.loader.LoaderBase;
import org.jetbrains.annotations.NotNull;

public class Stage1Loader extends LoaderBase {
    public Stage1Loader() {
        super("stage1", Stage1Loader.class.getPackage().getImplementationVersion());
    }

    @Override
    public @NotNull ILoader getNextLoader() {
        return null;
    }
}