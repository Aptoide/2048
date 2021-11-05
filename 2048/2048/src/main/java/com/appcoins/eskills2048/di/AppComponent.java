package com.appcoins.eskills2048.di;

import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module(includes = { AppModule.class, RepositoryModule.class, UseCaseModule.class })
@InstallIn(SingletonComponent.class) final class AppComponent {
}
