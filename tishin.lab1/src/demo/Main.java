package demo;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Main {

    public static ArrayList<File> getAllFiles(String source_path){
        File source_dir = new File(source_path);
        ArrayList<File> list = new ArrayList<File>();
        getFilesFromDir(source_dir.listFiles(), list);
        return list;
    }

    private static void getFilesFromDir(File[] dir, ArrayList<File> out){
        for (File file : dir){
            if (file.isDirectory()){
                getFilesFromDir(file.listFiles(), out);
            }
            else out.add(file);
        }
    }

    public static void main(String[] args) {

        String program_mode = args[2];
        if (program_mode.equals("archive")){
            try {
                String source_path = args[0];
                String archive_name = args[1];

                if (source_path.indexOf(source_path.length() - 1) != '\\') {
                    source_path = source_path.concat("\\");
                }

                File archive = new File(archive_name);
                ZipOutputStream os = new ZipOutputStream(new FileOutputStream(archive));
                ArrayList<File> list = getAllFiles(source_path);
                for (File file : list){
                    String fileName = file.toString().replace(source_path,"");
                    ZipEntry entry = new ZipEntry(fileName);
                    os.putNextEntry(entry);
                    FileInputStream is = new FileInputStream(file);
                    try {
                        byte[] buff = new byte[1024];
                        int n;
                        while ((n = is.read(buff, 0, 1024)) > -1)
                            os.write(buff, 0, n);
                    } finally {
                        os.closeEntry();
                    }
                }
                os.close();
            }
            catch (IOException exc){
            }
        }
        else if (program_mode.equals("dearchive")) {
            String source_file_name = args[0];
            String destination_path = args[1];

            if (destination_path.indexOf(destination_path.length() - 1) != '\\') {
                destination_path = destination_path.concat("\\");
            }

            try {
                ZipFile zip = new ZipFile(new File(source_file_name));
                Enumeration e = zip.entries();
                while (e.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) e.nextElement();

                    File new_file = new File(destination_path.concat(entry.getName()));

                    if (new_file.toString().lastIndexOf(".") == -1) {
                        new_file.mkdir();
                    } else {
                        File dir = new_file.getParentFile();
                        if (dir != null)
                            dir.mkdirs();
                        InputStream is = zip.getInputStream(entry);
                        FileOutputStream fos = new FileOutputStream(new_file);
                        try {
                            byte[] buff = new byte[1024];
                            int n;
                            while ((n = is.read(buff, 0, 1024)) > -1)
                                fos.write(buff, 0, n);
                        } finally {
                            fos.close();
                        }
                    }

                }
            } catch (ZipException exc) {

            } catch (IOException exc) {

            }
        }
    }
}
