package com.example.ezralendingapi.service;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import com.jcraft.jsch.*;

public class DatabaseDumpService {
    //Database credentials
    String db_host = "localhost";
    Integer  db_port = 3306;
    String db_user = "root";
    String db_password = "";
    String db_name = "ezradb";
    public void generateDatabaseDumpAndTransferToSFTP(String sftpHost, int sftpPort, String sftpUser,
                                                      String sftpPassword, String sftpDirectory) throws JSchException, SftpException, RuntimeException {
        String dumpFileName = "database_dump_" + LocalDateTime.now().toString() + ".sql";


        // Generate the database dump
        String dumpCommand = "mysqldump -h " + db_host + " -P " + db_port + " -u " + db_user + " -p" + db_password
                + " " + db_name + " > " + dumpFileName;
        executeCommand(dumpCommand);

        // Transfer the dump file to SFTP server
        JSch jsch = new JSch();
        Session session = jsch.getSession(sftpUser, sftpHost, sftpPort);
        session.setPassword(sftpPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        channelSftp.cd(sftpDirectory);

        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(dumpFileName))) {
            channelSftp.put(dumpFileName, outputStream.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        channelSftp.disconnect();
        session.disconnect();

        // Cleanup
        deleteDumpFile(dumpFileName);
    }

    private void executeCommand(String command) throws JSchException {
        // Code to execute the command using ProcessBuilder or other means
    }

    private void deleteDumpFile(String dumpFileName) {
        // Code to delete the generated dump file
    }
}
