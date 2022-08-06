package top.xinsin.thread;

import top.xinsin.entity.Artifact;
import top.xinsin.entity.AssetEntity;
import top.xinsin.entity.NativeFileEntity;
import top.xinsin.http.HttpVillager;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created On 8/6/22 10:30 AM
 *
 * @author xinsin
 * @version 1.0.0
 */
public class DownloadAssetsMultipleThread {
    private static DownloadAssetsMultipleThread downloadAssetsMultipleThread;
    public ExecutorService executorServiceAssets = Executors.newFixedThreadPool(10);
    public CountDownLatch countDownLatchAssets = null;
    public ExecutorService executorServiceLibraries = Executors.newFixedThreadPool(10);
    public CountDownLatch countDownLatchLibraries = null;

    private DownloadAssetsMultipleThread() {
    }
    public void downloadAssets(ArrayList<AssetEntity> assetEntities) {
        countDownLatchAssets = new CountDownLatch(assetEntities.size());
        assetEntities.forEach(e -> {
            executorServiceAssets.submit(()-> {
                Thread.currentThread().setName("downloadAssetsThread: " + Thread.currentThread().getId());
                new HttpVillager().assetDownload(e.getHash(),e.getSize());
                countDownLatchAssets.countDown();
            });
        });
    }
    public void downloadLibraries(ArrayList<Artifact> artifacts) {
        countDownLatchLibraries = new CountDownLatch(artifacts.size());
        artifacts.forEach(e -> {
            executorServiceLibraries.submit(()-> {
                Thread.currentThread().setName("downloadLibrariesThread: " + Thread.currentThread().getId());
                new HttpVillager().librariesDownload(e.getUrl(),e.getPath(),e.getSize());
                countDownLatchLibraries.countDown();
            });
        });
    }
//    DCL单例模式
    public static DownloadAssetsMultipleThread getInstance() {
        if (downloadAssetsMultipleThread == null) {
            synchronized (DownloadAssetsMultipleThread.class) {
                if (downloadAssetsMultipleThread == null) {
                    downloadAssetsMultipleThread = new DownloadAssetsMultipleThread();
                }
            }
        }
        return downloadAssetsMultipleThread;
    }
}
