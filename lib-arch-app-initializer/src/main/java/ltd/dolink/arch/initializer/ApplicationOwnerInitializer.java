package ltd.dolink.arch.initializer;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ProcessLifecycleInitializer;
import androidx.startup.Initializer;

import java.util.Arrays;
import java.util.List;

public class ApplicationOwnerInitializer implements Initializer<ApplicationOwner> {

    @NonNull
    @Override
    public ApplicationOwner create(@NonNull Context context) {
        Application application = (Application) context.getApplicationContext();
        ApplicationOwner applicationOwner = new ApplicationOwner(application);
        return applicationOwner;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Arrays.asList(ProcessLifecycleInitializer.class);
    }

}
