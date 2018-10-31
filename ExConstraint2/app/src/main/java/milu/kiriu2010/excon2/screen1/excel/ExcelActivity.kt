package milu.kiriu2010.excon2.screen1.excel

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_excel.*
import milu.kiriu2010.excon2.R
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.FileNotFoundException
import java.io.IOException
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.streaming.SXSSFSheet
import java.io.File
import java.lang.StringBuilder
import java.util.*
import kotlin.math.log


class ExcelActivity : AppCompatActivity() {

    // Excelのヘッダ
    private val LIST_ALPHA = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")

    // Excel保存先ファイル
    private lateinit var file: File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excel)

        // アップロード
        btnUpload.setOnClickListener {

            // Excelファイル保存先ファイル名
            file = File(this.filesDir, "out.xlsx")

            // Excelファイル作成
            createExcel()

            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                putExtra(Intent.EXTRA_TITLE,"アップロード")
                putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file.path))
                startActivity(intent)
            }
        }

        // 数値計算
        val sb = StringBuilder()
        // sinの計算
        val angleArray = intArrayOf(0,30,45,60,90,120,135,150,180)
        angleArray.forEach {
            sb.append( "sin(%d)=%.7f\n".format(it,Math.sin(Math.toRadians(it.toDouble()))) )
        }
        // log2の計算
        val logArray = intArrayOf(2,4,8,16)
        logArray.forEach {
            sb.append( "log2(%d)=%.7f\n".format(it,log(it.toDouble(),2.0)) )
        }
        textViewMath.setText(sb.toString())
    }

    @Throws(FileNotFoundException::class,IOException::class)
    private fun createExcel() {
        var book: Workbook? = null
        try {
            book = SXSSFWorkbook()

            val font = book.createFont()
            font.fontName = "ＭＳ ゴシック"
            font.fontHeightInPoints = 9.toShort()

            val format = book.createDataFormat()

            //ヘッダ文字列用のスタイル
            val style_header = book.createCellStyle()
            style_header.borderBottom = BorderStyle.THIN
            setBorder(style_header, BorderStyle.THIN)
            style_header.fillForegroundColor = HSSFColor.HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE.index
            style_header.fillPattern = FillPatternType.SOLID_FOREGROUND
            style_header.verticalAlignment = VerticalAlignment.TOP
            style_header.setFont(font)

            //文字列用のスタイル
            val style_string = book.createCellStyle()
            setBorder(style_string, BorderStyle.THIN)
            style_string.verticalAlignment = VerticalAlignment.TOP
            style_string.setFont(font)

            //改行が入った文字列用のスタイル
            val style_string_wrap = book.createCellStyle()
            setBorder(style_string_wrap, BorderStyle.THIN)
            style_string_wrap.verticalAlignment = VerticalAlignment.TOP
            style_string_wrap.wrapText = true
            style_string_wrap.setFont(font)

            //整数用のスタイル
            val style_int = book.createCellStyle()
            setBorder(style_int, BorderStyle.THIN)
            style_int.dataFormat = format.getFormat("#,##0;-#,##0")
            style_int.verticalAlignment = VerticalAlignment.TOP
            style_int.setFont(font)

            //小数用のスタイル
            val style_double = book.createCellStyle()
            setBorder(style_double, BorderStyle.THIN)
            style_double.dataFormat = format.getFormat("#,##0.0;-#,##0.0")
            style_double.verticalAlignment = VerticalAlignment.TOP
            style_double.setFont(font)

            //円表示用のスタイル
            val style_yen = book.createCellStyle()
            setBorder(style_yen, BorderStyle.THIN)
            style_yen.dataFormat = format.getFormat("\"\\\"#,##0;\"\\\"-#,##0")
            style_yen.verticalAlignment = VerticalAlignment.TOP
            style_yen.setFont(font)

            //パーセント表示用のスタイル
            val style_percent = book.createCellStyle()
            setBorder(style_percent, BorderStyle.THIN)
            style_percent.dataFormat = format.getFormat("0.0%")
            style_percent.verticalAlignment = VerticalAlignment.TOP
            style_percent.setFont(font)

            //日時表示用のスタイル
            val style_datetime = book.createCellStyle()
            setBorder(style_datetime, BorderStyle.THIN)
            style_datetime.dataFormat = format.getFormat("yyyy/mm/dd hh:mm:ss")
            style_datetime.verticalAlignment = VerticalAlignment.TOP
            style_datetime.setFont(font)


            // シートの作成(3シート作ってみる)
            (0..3).forEach { i ->
                val sheet: Sheet = book.createSheet()
                if (sheet is SXSSFSheet) {
                    (sheet as SXSSFSheet).trackAllColumnsForAutoSizing()
                }

                //シート名称の設定
                book.setSheetName(i, "シート" + (i + 1))

                //ヘッダ行の作成
                var rowNumber = 0
                var colNumber = 0
                var row = sheet.createRow(rowNumber)
                var cell = row.createCell(colNumber++)
                cell.setCellStyle(style_header)
                cell.setCellType(CellType.STRING)
                cell.setCellValue("No.")

                cell = row.createCell(colNumber++)
                cell.setCellStyle(style_header)
                cell.setCellType(CellType.STRING)
                cell.setCellValue("文字列")

                cell = row.createCell(colNumber++)
                cell.setCellStyle(style_header)
                cell.setCellType(CellType.STRING)
                cell.setCellValue("改行の入った文字列")

                cell = row.createCell(colNumber++)
                cell.setCellStyle(style_header)
                cell.setCellType(CellType.STRING)
                cell.setCellValue("整数")

                cell = row.createCell(colNumber++)
                cell.setCellStyle(style_header)
                cell.setCellType(CellType.STRING)
                cell.setCellValue("小数")

                cell = row.createCell(colNumber++)
                cell.setCellStyle(style_header)
                cell.setCellType(CellType.STRING)
                cell.setCellValue("円")

                cell = row.createCell(colNumber++)
                cell.setCellStyle(style_header)
                cell.setCellType(CellType.STRING)
                cell.setCellValue("パーセント")

                cell = row.createCell(colNumber++)
                cell.setCellStyle(style_header)
                cell.setCellType(CellType.STRING)
                cell.setCellValue("日時")

                cell = row.createCell(colNumber)
                cell.setCellStyle(style_header)
                cell.setCellType(CellType.STRING)
                cell.setCellValue("円(8%の税込)")

                //ウィンドウ枠の固定
                sheet.createFreezePane(1, 1)

                //ヘッダ行にオートフィルタの設定
                sheet.setAutoFilter(CellRangeAddress(0, 0, 0, colNumber))

                //列幅の自動調整
                (0..colNumber).forEach {
                    sheet.autoSizeColumn(it, true)
                }

                //データ行の生成(10行作ってみる)
                (0..10).forEach {j ->
                    rowNumber++
                    colNumber = 0
                    row = sheet.createRow(rowNumber)
                    cell = row.createCell(colNumber++)
                    cell.cellStyle = style_int
                    cell.cellType = CellType.NUMERIC
                    cell.setCellValue((j + 1).toString() )

                    cell = row.createCell(colNumber++)
                    cell.cellStyle = style_string
                    cell.cellType = CellType.STRING
                    cell.setCellValue("これは" + (j + 1) + "行目のデータです。")

                    cell = row.createCell(colNumber++)
                    cell.cellStyle = style_string_wrap
                    cell.cellType = CellType.STRING
                    cell.setCellValue("これは\n" + (j + 1) + "行目の\nデータです。")

                    cell = row.createCell(colNumber++)
                    cell.cellStyle = style_int
                    cell.cellType = CellType.STRING
                    cell.setCellValue(((j + 1) * 1000).toString())

                    cell = row.createCell(colNumber++)
                    cell.cellStyle = style_double
                    cell.cellType = CellType.STRING
                    cell.setCellValue((j + 1) as Double * 1000)

                    cell = row.createCell(colNumber++)
                    cell.cellStyle = style_yen
                    cell.cellType = CellType.STRING
                    cell.setCellValue(((j + 1) * 1000).toString())

                    cell = row.createCell(colNumber++)
                    cell.cellStyle = style_percent
                    cell.cellType = CellType.STRING
                    cell.setCellValue((j + 1) as Double)

                    cell = row.createCell(colNumber++)
                    cell.cellStyle = style_datetime
                    cell.cellType = CellType.STRING
                    cell.setCellValue(Date())

                    cell = row.createCell(colNumber)
                    cell.cellStyle = style_yen
                    cell.cellType = CellType.FORMULA
                    cell.cellFormula = "ROUND(" + getExcelColumnString(colNumber - 3) + (rowNumber + 1) + "*1.08, 0)"

                    //列幅の自動調整
                    (0..colNumber).forEach {k->
                        sheet.autoSizeColumn(k,true)
                    }
                }
            }

            // シート３を消してみる
            book.removeSheetAt(2)

            // ファイル出力
            file.outputStream().use {
                book.write(it)
            }
        }
        finally {
            // SXSSFWorkbookはメモリ空間を節約する代わりにテンポラリファイルを大量に生成するため、
            // 不要になった段階でdisposeしてテンポラリファイルを削除する必要がある
            (book as? SXSSFWorkbook)?.dispose()
        }
    }

    private fun setBorder(style: CellStyle, border: BorderStyle) {
        style.borderBottom = border
        style.borderTop = border
        style.borderLeft = border
        style.borderRight = border
    }

    fun getExcelColumnString(column: Int): String {
        var result = ""

        if (column >= 0) {
            if (column / LIST_ALPHA.size > 0) {
                result += getExcelColumnString(column / LIST_ALPHA.size - 1)
            }
            result += LIST_ALPHA[column % LIST_ALPHA.size]
        }

        return result
    }

}
