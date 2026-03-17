package lunacrew.luna.backup

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.sqlite.db.SupportSQLiteOpenHelper
import lunacrew.luna.util.extensions.log
import net.lingala.zip4j.ZipFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import javax.inject.Inject
import kotlin.system.exitProcess

class RoomBackup @Inject constructor(
    val db: SupportSQLiteOpenHelper,
) {
    /**
     * Exports a database to a zip file
     */
    fun export(
        context: Context,
        backupFileUri: Uri,
    ) {
        try {
            val dbFiles = getDbFiles()
            createCheckpoint()

            ZipFile(dbFiles.backupZipFile).addFiles(listOf(dbFiles.dbFile, dbFiles.walFile, dbFiles.shmFile))

            val outputStream = context.contentResolver.openOutputStream(backupFileUri)!!
            Files.copy(dbFiles.backupZipFile?.toPath(), outputStream)
        } catch (e: IOException) {
            e.log("RoomBackup::export")
        }
    }

    /**
     * Imports a database from a zip file
     */
    fun import(
        context: Context,
        backupFileUri: Uri,
        restart: Boolean = true,
    ) {
        try {
            val dbFiles = getDbFiles()

            dbFiles.backupZipFile?.delete()

            val inputStream = context.contentResolver.openInputStream(backupFileUri)!!
            Files.copy(inputStream, dbFiles.backupZipFile?.toPath())

            dbFiles.dbFile.delete()
            dbFiles.walFile?.delete()
            dbFiles.shmFile?.delete()

            ZipFile(dbFiles.backupZipFile).extractAll(dbFiles.dbFile.parent!!)
            createCheckpoint()
        } catch (e: IOException) {
            e.log("RoomBackup::import")
        }

        if (restart) {
            val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
            exitProcess(0)
        }
    }

    private fun createCheckpoint() {
        val db = db.writableDatabase
        db.query("PRAGMA wal_checkpoint(FULL);")
        db.query("PRAGMA wal_checkpoint(TRUNCATE);")
    }

    data class DbFiles(
        val dbFile: File,
        val walFile: File?,
        val shmFile: File?,
        val backupZipFile: File?,
    )

    private fun getDbFiles() =
        DbFiles(
            dbFile = File(db.readableDatabase.path!!),
            walFile = File(db.readableDatabase.path + "-wal"),
            shmFile = File(db.readableDatabase.path + "-shm"),
            backupZipFile = File(db.readableDatabase.path + ".zip"),
        )
}
