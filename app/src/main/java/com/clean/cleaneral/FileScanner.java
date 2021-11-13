package com.clean.cleaneral;

import static com.clean.cleaneral.ui.activities.WhitelistActivity.getWhiteList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.clean.cleaneral.ui.activities.MainActivity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FileScanner {

    public static boolean isRunning = false;

    SharedPreferences prefs;
    private Context context;
    private final File path;
    private Resources res;
    private com.clean.cleaneral.databinding.FragmentLoadingBinding gui;
    private int filesRemoved = 0;
    private long kilobytesTotal = 0;
    private boolean delete = false;
    private boolean emptyDir = false;
    private boolean autoWhite = true;
    private boolean corpse = false;
    private static final ArrayList<String> filters = new ArrayList<>();
    private static final String[] protectedFileList = {
            "backup", "copy", "copies", "important", "do_not_edit"};

    public FileScanner(File path, Context context) {
        this.path = path;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        getWhiteList();
    }
    public static String convertSize(long length) {
        final DecimalFormat format = new DecimalFormat("#.##");
        final long MiB = 1024 * 1024;
        final long KiB = 1024;

        if (length > MiB) {
            return format.format(length / MiB) + " MB";
        }
        if (length > KiB) {
            return format.format(length / KiB) + " KB";
        }
        return format.format(length) + " B";
    }
    private List<File> getListFiles() {
        return getListFiles(path);
    }

    /**
     * Used to generate a list of all files on device
     * @param parentDirectory where to start searching from
     * @return List of all files on device (besides whitelisted ones)
     */
    private synchronized List<File> getListFiles(File parentDirectory) {
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files = parentDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file != null) { // hopefully to fix crashes on a very limited number of devices.
                    if (!isWhiteListed(file)) { // won't touch if whitelisted
                        if (file.isDirectory()) { // folder

                            if (autoWhite) {
                                if (!autoWhiteList(file))
                                    inFiles.add(file);
                            }
                            else inFiles.add(file); // add folder itself

                            inFiles.addAll(getListFiles(file)); // add contents to returned list

                        } else inFiles.add(file); // add file
                    }
                }
            }
        }

        return inFiles;
    }

    /**
     * Runs a for each loop through the white list, and compares the path of the file
     * to each path in the list
     * @param file file to check if in the whitelist
     * @return true if is the file is in the white list, false if not
     */
    private synchronized boolean isWhiteListed(File file) {
        for (String path : getWhiteList())
            if (path.equalsIgnoreCase(file.getAbsolutePath())
                    || path.equalsIgnoreCase(file.getName()))
                return true;
        return false;
    }

    /**
     * Runs before anything is filtered/cleaned. Automatically adds folders to the whitelist
     * based on the name of the folder itself
     * @param file file to check whether it should be added to the whitelist
     */
    private synchronized boolean autoWhiteList(File file) {

        for (String protectedFile : protectedFileList) {
            if (file.getName().toLowerCase().contains(protectedFile) &&
                    !getWhiteList().contains(file.getAbsolutePath().toLowerCase())) {
                getWhiteList().add(file.getAbsolutePath().toLowerCase());
                prefs.edit().putStringSet("whitelist", new HashSet<>(getWhiteList())).apply();
                return true;
            }
        }

        return false;
    }

    /**
     * Runs as for each loop through the filter, and checks if
     * the file matches any filters
     * @param file file to check
     * @return true if the file's extension is in the filter, false otherwise
     */
    public synchronized boolean filter(File file) {
        if (file != null) {
            // corpse checking - TODO: needs improved!
            if (file.getParentFile() != null && file.getParentFile().getParentFile() != null&& corpse) {
                if (file.getParentFile().getName().equals("data") && file.getParentFile().getParentFile().getName().equals("Android"))
                    if (!getInstalledPackages().contains(file.getName()) && !file.getName().equals(".nomedia"))
                        return true;
            }

            if (file.isDirectory()) {
                if (isDirectoryEmpty(file) && emptyDir) return true; // empty folder
            }

            for (String filter : filters)
                if (file.getAbsolutePath().toLowerCase()
                        .matches(filter.toLowerCase()))
                    return true; // file
        }

        return false; // not empty folder or file in filter
    }

    private synchronized List<String> getInstalledPackages() {
        final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        List<String> packagesString = new ArrayList<>();
        for (ApplicationInfo packageInfo : packages) {
            packagesString.add(packageInfo.packageName);
        }
        return packagesString;
    }

    /**
     * lists the contents of the file to an array, if the array length is 0, then return true,
     * else false
     * @param directory directory to test
     * @return true if empty, false if containing a file(s)
     */
    private synchronized boolean isDirectoryEmpty(File directory) {

        if (directory.list() != null && directory.list() != null) return Objects.requireNonNull(directory.list()).length == 0;
        else return false;
    }

    /**
     * Adds paths to the white list that are not to be cleaned. As well as adds
     * extensions to filter. 'generic', 'aggressive', and 'apk' should be assigned
     * by calling preferences.getBoolean()
     */
    @SuppressLint("ResourceType")
    public synchronized FileScanner setUpFilters(boolean generic, boolean aggressive, boolean apk) {
        List<String> folders = new ArrayList<>();
        List<String> files = new ArrayList<>();

        setResources(context.getResources());

        if (generic) {
            folders.addAll(Arrays.asList(res.getStringArray(R.array.generic_filter_folders)));
            files.addAll(Arrays.asList(res.getStringArray(R.array.generic_filter_files)));
        }

        if (aggressive) {
            folders.addAll(Arrays.asList(res.getStringArray(R.array.aggressive_filter_folders)));
            files.addAll(Arrays.asList(res.getStringArray(R.array.aggressive_filter_files)));
        }

        // filters
        filters.clear();
        for (String folder : folders)
            filters.add(getRegexForFolder(folder));
        for (String file : files)
            filters.add(getRegexForFile(file));

        // apk
        if (apk) filters.add(getRegexForFile(".apk"));

        return this;
    }
    public long startScan() {
        FileScanner.isRunning = true;
        byte cycles = 0;
        byte maxCycles = 10;
        List<File> foundFiles;
        if (!delete) maxCycles = 1; // when nothing is being deleted. Stops duplicates from being found

        // removes the need to 'clean' multiple times to get everything
        while (cycles < maxCycles) {

            // find files
            foundFiles = getListFiles();
            if (gui != null) gui.pbProgress.setMax(gui.pbProgress.getMax() + foundFiles.size());

            // scan & delete
            for (File file : foundFiles) {
                if (filter(file)) { // filter

                    if (delete) {
                        kilobytesTotal += file.length();
                        ++filesRemoved;
                        if (!file.delete()) { // deletion

                        }
                    } else {
                        kilobytesTotal += file.length();
                    }
                }
            }

            if (filesRemoved == 0){

                break;} // nothing found this run, no need to run again

            filesRemoved = 0; // reset for next cycle
            ++cycles;
        }

        FileScanner.isRunning = false;
        return kilobytesTotal;
    }

    private String getRegexForFolder(String folder) {
        return ".*(\\\\|/)" + folder + "(\\\\|/|$).*";
    }

    private String getRegexForFile(String file) {
        return ".+"+ file.replace(".", "\\.") + "$";
    }

    public FileScanner setGUI(com.clean.cleaneral.databinding.FragmentLoadingBinding gui) {
        this.gui = gui;
        return this;
    }

    FileScanner setResources(Resources res) {
        this.res = res;
        return this;
    }

    public FileScanner setEmptyDir(boolean emptyDir) {
        this.emptyDir = emptyDir;
        return this;
    }

    public FileScanner setDelete(boolean delete) {
        this.delete = delete;
        return this;
    }

    public FileScanner setCorpse(boolean corpse) {
        this.corpse = corpse;
        return this;
    }

    public FileScanner setAutoWhite(boolean autoWhite) {
        this.autoWhite = autoWhite;
        return this;
    }

    public FileScanner setContext(Context context) {
        this.context = context;
        return this;
    }
}
