package harahap;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class laporan_absensi_controller implements javafx.fxml.Initializable {
    public static laporan_absensi_controller INSTANCE;
    
    // Navigation buttons
    @FXML private Button button_jadwal_mengajar;
    @FXML private Button button_laporan_gaji;
    @FXML private Button button_absensi;
    @FXML private Button button_crud_pelatih;
    @FXML private Button button_crud_siswa;
    @FXML private Button button_crud_tim_siswa;

    // Main absence table
    @FXML private TableView<laporan_absensi_data> absensi;
    @FXML private TableColumn<laporan_absensi_data, Integer> id_pelatih;
    @FXML private TableColumn<laporan_absensi_data, String> nama_pelatih;
    @FXML private TableColumn<laporan_absensi_data, Integer> Jumlah_Sesi_Yang_Dijadwalkan;
    @FXML private TableColumn<laporan_absensi_data, Double> persen_hadir;
    @FXML private TableColumn<laporan_absensi_data, Integer> total_jam;
    @FXML private ObservableList<laporan_absensi_data> laporan_absensi_data = FXCollections.observableArrayList();

    // Date filter controls
    @FXML private DatePicker dari_tanggal;
    @FXML private DatePicker sampai_tanggal;
    @FXML private Button tanggal_sekarang;
    @FXML private Button reset_tanggal;
    @FXML private Button print_laporan;

    // DB URL
    private static final String DB_URL = "jdbc:sqlite:database/harahap.db";

    // Navigation helpers
    public void switch_to_jadwal_mengajar(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_jadwal_mengajar(e);
    }
    public void switch_to_crud_pelatih(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_crud_pelatih(e);
    }
    public void switch_to_crud_siswa(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_crud_siswa(e);
    }
    public void switch_to_crud_tim_siswa(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_crud_tim_siswa(e);
    }
    public void switch_to_crud_absensi(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_crud_absensi(e);
    }
    public void switch_to_laporan_gaji(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_laporan_gaji(e);
    }
    public void switch_to_laporan_absensi(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_laporan_absensi(e);
    }
    public void switch_to_laporan_grade(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_laporan_grade(e);
    }
    public void switch_to_laporan_pengecualian(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_laporan_pengecualian(e);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Absence table bindings
        id_pelatih.setCellValueFactory(new PropertyValueFactory<>("id_pelatih"));
        nama_pelatih.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
        Jumlah_Sesi_Yang_Dijadwalkan.setCellValueFactory(new PropertyValueFactory<>("scheduled_sessions"));
        persen_hadir.setCellValueFactory(new PropertyValueFactory<>("hadir_percent"));
        total_jam.setCellValueFactory(new PropertyValueFactory<>("delivered_hours"));

        INSTANCE = this;

        setupDateControls();
        
        // Load initial absence data
        readDataLaporanAbsensi();
    }

    // -------------------------
    // Absence report table
    // -------------------------
    public void readDataLaporanAbsensi() {
        laporan_absensi_data.clear();

        LocalDate from = (dari_tanggal != null) ? dari_tanggal.getValue() : null;
        LocalDate to = (sampai_tanggal != null) ? sampai_tanggal.getValue() : null;

        String sql = ""
            + "SELECT "
            + "  p.id_pelatih, "
            + "  p.nama_pelatih, "
            + "  COUNT(j.id_jadwal) AS scheduled_sessions, "
            + "  SUM(CASE WHEN j.kehadiran_pelatih = 'HADIR' THEN 1 ELSE 0 END) AS hadir_sessions, "
            + "  ROUND(100.0 * SUM(CASE WHEN j.kehadiran_pelatih = 'HADIR' THEN 1 ELSE 0 END) / COUNT(j.id_jadwal), 2) AS hadir_percent, "
            + "  SUM(COALESCE(j.total_jam, 0)) AS delivered_hours "
            + "FROM jadwal j "
            + "JOIN pelatih p ON j.id_pelatih = p.id_pelatih "
            + "WHERE 1=1 ";

        if (from != null && to != null) {
            sql += "AND j.tanggal BETWEEN ? AND ? ";
        } else if (from != null) {
            sql += "AND j.tanggal >= ? ";
        } else if (to != null) {
            sql += "AND j.tanggal <= ? ";
        }

        sql += "GROUP BY p.id_pelatih, p.nama_pelatih "
             + "ORDER BY p.nama_pelatih ASC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int paramIndex = 1;
            if (from != null && to != null) {
                ps.setString(paramIndex++, from.toString());
                ps.setString(paramIndex++, to.toString());
            } else if (from != null) {
                ps.setString(paramIndex++, from.toString());
            } else if (to != null) {
                ps.setString(paramIndex++, to.toString());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idPelatih = rs.getInt("id_pelatih");
                    String namaPelatih = rs.getString("nama_pelatih");
                    int scheduledSessions = rs.getInt("scheduled_sessions");
                    double hadirPercent = rs.getDouble("hadir_percent");
                    int deliveredHours = rs.getObject("delivered_hours") != null ? rs.getInt("delivered_hours") : 0;

                    laporan_absensi_data row = new laporan_absensi_data(
                        idPelatih,
                        namaPelatih,
                        scheduledSessions,
                        hadirPercent,
                        deliveredHours
                    );
                    laporan_absensi_data.add(row);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        absensi.setItems(laporan_absensi_data);
    }

    // -------------------------
    // Date controls setup
    // -------------------------
    private void setupDateControls() {
        // Button: set both datepickers to today
        if (tanggal_sekarang != null) {
            tanggal_sekarang.setOnAction(e -> {
                LocalDate today = LocalDate.now();
                if (dari_tanggal != null) dari_tanggal.setValue(today);
                if (sampai_tanggal != null) sampai_tanggal.setValue(today);
                readDataLaporanAbsensi();
            });
        }

        // Button: reset both datepickers to null
        if (reset_tanggal != null) {
            reset_tanggal.setOnAction(e -> {
                if (dari_tanggal != null) dari_tanggal.setValue(null);
                if (sampai_tanggal != null) sampai_tanggal.setValue(null);
                readDataLaporanAbsensi();
            });
        }

        // When either datepicker changes, reload table with appropriate range
        if (dari_tanggal != null) {
            dari_tanggal.valueProperty().addListener((obs, oldV, newV) -> {
                LocalDate from = newV;
                LocalDate to = (sampai_tanggal != null) ? sampai_tanggal.getValue() : null;
                applyDateFilterAndReload(from, to);
            });
        }
        if (sampai_tanggal != null) {
            sampai_tanggal.valueProperty().addListener((obs, oldV, newV) -> {
                LocalDate to = newV;
                LocalDate from = (dari_tanggal != null) ? dari_tanggal.getValue() : null;
                applyDateFilterAndReload(from, to);
            });
        }
    }

    private void applyDateFilterAndReload(LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            // ensure from <= to
            if (from.isAfter(to)) {
                // swap so range is valid
                LocalDate tmp = from;
                from = to;
                to = tmp;
            }
        }
        readDataLaporanAbsensi();
    }

    // -------------------------
    // Excel export / Print
    // -------------------------
    @FXML
    private void handlePrintLaporan(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Absence Report As");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Excel Workbook (*.xlsx)", "*.xlsx"));

        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
        String suggestedName = "laporan_absensi_" + dateStr;
        fileChooser.setInitialFileName(suggestedName + ".xlsx");

        File file = fileChooser.showSaveDialog(print_laporan.getScene().getWindow());
        if (file == null) return;

        try {
            exportTableToFormattedExcel(absensi, file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void exportTableToFormattedExcel(TableView<?> table, File file) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Laporan Pengecualian");
            
            int currentRow = 0;
            
            // ===== HEADER SECTION =====
            // Create blue background for header cells
            XSSFCellStyle headerStyle = workbook.createCellStyle();
            XSSFColor blueColor = new XSSFColor(new byte[]{(byte)0, (byte)51, (byte)102}); // Dark blue
            headerStyle.setFillForegroundColor(blueColor);
            headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setWrapText(true);
            
            // Company name with bold white font
            XSSFFont companyFont = workbook.createFont();
            companyFont.setBold(true);
            companyFont.setFontHeightInPoints((short) 11);
            companyFont.setColor(new XSSFColor(new byte[]{(byte)255, (byte)255, (byte)255})); // White
            headerStyle.setFont(companyFont);
            headerStyle.setAlignment(HorizontalAlignment.LEFT);
            
            // Set blue background for header rows (0-3)
            for (int row = 0; row < 8; row++) {
                Row hRow = sheet.createRow(row);
                hRow.setHeightInPoints(row == 0 ? 50 : 15);
                for (int col = 0; col < 8; col++) {
                    Cell cell = hRow.createCell(col);
                    XSSFCellStyle bgStyle = workbook.createCellStyle();
                    bgStyle.setFillForegroundColor(blueColor);
                    bgStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
                    cell.setCellStyle(bgStyle);
                }
            }
            
            // Merge cells for company text (rows 0-4, columns 1-5)
            sheet.addMergedRegion(new CellRangeAddress(0, 4, 1, 8));
            Row headerRow = sheet.createRow(0);
            Cell headerCell = headerRow.getCell(1);
            if (headerCell == null) {
                headerCell = headerRow.createCell(1);
            }
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("PT HARAHAP SWIMMING SCHOOL\nRT.12/RW.3, Srengseng Sawah, Jagakarsa, Kota Jakarta Selatan, DKI Jakarta 12630\nTelp: 088999889908");
            
            // Add company logo/image on the left
            try {
                URL iconUrl = getClass().getResource("/harahap/images/icon.png");
                if (iconUrl != null) {
                    File imageFile = new File(iconUrl.toURI());
                    if (imageFile.exists()) {
                        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                        int pictureIndex = workbook.addPicture(imageBytes, XSSFWorkbook.PICTURE_TYPE_PNG);
                        
                        Drawing<?> drawing = sheet.createDrawingPatriarch();
                        // Position image in columns 0-1, rows 0-4 (top-left of header)
                        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 0, 1, 5);
                        Picture picture = drawing.createPicture(anchor, pictureIndex);
                        picture.resize(1.0);
                    }
                }
            } catch (Exception ex) {
                System.out.println("Could not load company logo: " + ex.getMessage());
            }
            
            currentRow = 5;
            
            // Get column count early for title merging
            int colCount = table.getColumns().size();
            
            // Add report title - centered across all columns
            sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow + 1, 0, 8));
            Row titleRow = sheet.createRow(currentRow++);
            Cell titleCell = titleRow.createCell(0);
            XSSFFont titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            XSSFCellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleCell.setCellStyle(titleStyle);
            titleCell.setCellValue("LAPORAN PENGECUALIAN GAJI");
            
            currentRow++; // Empty row
            
            // Add date info
            Row dateRow = sheet.createRow(currentRow++);
            LocalDate from = (dari_tanggal != null) ? dari_tanggal.getValue() : null;
            LocalDate to = (sampai_tanggal != null) ? sampai_tanggal.getValue() : null;
            String dateRange = "Periode: ";
            if (from != null && to != null) {
                dateRange += from + " hingga " + to;
            } else if (from != null) {
                dateRange += "Dari " + from;
            } else if (to != null) {
                dateRange += "Hingga " + to;
            } else {
                dateRange += "Semua Tanggal";
            }
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue(dateRange);
            
            currentRow++; // Empty row
            
            // Add table header row
            Row headerRowTable = sheet.createRow(currentRow++);
            XSSFCellStyle headerTableStyle = workbook.createCellStyle();
            XSSFColor lightBlue = new XSSFColor(new byte[]{(byte)0, (byte)102, (byte)204}); // Light blue
            headerTableStyle.setFillForegroundColor(lightBlue);
            headerTableStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
            XSSFFont tableHeaderFont = workbook.createFont();
            tableHeaderFont.setBold(true);
            tableHeaderFont.setColor(new XSSFColor(new byte[]{(byte)255, (byte)255, (byte)255})); // White
            headerTableStyle.setFont(tableHeaderFont);
            headerTableStyle.setAlignment(HorizontalAlignment.CENTER);
            headerTableStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerTableStyle.setBorderBottom(BorderStyle.THIN);
            headerTableStyle.setBorderTop(BorderStyle.THIN);
            headerTableStyle.setBorderLeft(BorderStyle.THIN);
            headerTableStyle.setBorderRight(BorderStyle.THIN);
            
            for (int c = 0; c < colCount; c++) {
                TableColumn<?, ?> col = table.getColumns().get(c);
                Cell cell = headerRowTable.createCell(c);
                cell.setCellStyle(headerTableStyle);
                cell.setCellValue(col.getText() != null ? col.getText() : "");
            }
            
            // Add data rows
            XSSFCellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            
            for (int r = 0; r < table.getItems().size(); r++) {
                Row row = sheet.createRow(currentRow++);
                for (int c = 0; c < colCount; c++) {
                    Object value = table.getColumns().get(c).getCellData(r);
                    Cell cell = row.createCell(c);
                    cell.setCellStyle(dataStyle);
                    
                    if (value == null) {
                        cell.setCellValue("");
                    } else if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
            
            // ===== FOOTER SECTION =====
            // Position footer at bottom of A4 page (approximately row 50)
            int footerRow = 50;
            
            // Add location and date
            Row footerDateRow = sheet.createRow(footerRow++);
            Cell footerDateCell = footerDateRow.createCell(8); // Place in last column
            XSSFCellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setAlignment(HorizontalAlignment.RIGHT);
            footerDateCell.setCellStyle(dateStyle);
            String footerDate = getIndonesianDate();
            footerDateCell.setCellValue(footerDate);
            
            // Empty rows for signature space
            footerRow += 2;
            
            // Add signature line for company owner
            Row signatureRow = sheet.createRow(footerRow++);
            Cell signatureCell = signatureRow.createCell(8); // Place in last column
            XSSFCellStyle signatureStyle = workbook.createCellStyle();
            signatureStyle.setAlignment(HorizontalAlignment.RIGHT);
            signatureCell.setCellStyle(signatureStyle);
            
            // Owner name placeholder
            Row ownerRow = sheet.createRow(footerRow++);
            Cell ownerCell = ownerRow.createCell(8); // Place in last column
            XSSFCellStyle ownerStyle = workbook.createCellStyle();
            ownerStyle.setAlignment(HorizontalAlignment.RIGHT);
            ownerCell.setCellStyle(ownerStyle);
            // TODO: Add company owner name here
            ownerCell.setCellValue("Nama Pemilik Perusahaan");
            
            // Set column widths to fit A4 paper width
            // A4 width is ~8.27 inches, with 0.5 inch margins = ~7.27 inches for content
            // This equals approximately 52 characters width for 6 columns
            int columnWidth = 18; // Width in character units
            for (int c = 0; c < colCount; c++) {
                sheet.setColumnWidth(c, columnWidth * 256); // POI uses 256 units per character
            }
            
            // Set print setup for A4 paper
            PrintSetup printSetup = sheet.getPrintSetup();
            printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE);
            printSetup.setLandscape(false); // Portrait orientation
            
            // Set page margins (in inches)
            sheet.setMargin(Sheet.TopMargin, 0.5);
            sheet.setMargin(Sheet.BottomMargin, 0.5);
            sheet.setMargin(Sheet.LeftMargin, 0.5);
            sheet.setMargin(Sheet.RightMargin, 0.5);
            
            // Set autobreak for page fitting
            sheet.setAutobreaks(true);
            
            // Set fit to one page width
            sheet.setFitToPage(true);
            sheet.setHorizontallyCenter(false);
            
            // // Set header for printing
            // Header header = sheet.getHeader();
            // header.setCenter("Laporan Pengecualian Gaji");
            
            // Write to file
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }
    
    private String getIndonesianDate() {
        LocalDate today = LocalDate.now();
        
        String[] months = {
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        };
        
        String[] days = {
            "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"
        };
        
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        int dayIndex = dayOfWeek.getValue() % 7; // 0 = Monday, convert to 0-6 where 0 is Monday
        String dayName = days[dayIndex];
        
        int day = today.getDayOfMonth();
        String month = months[today.getMonthValue() - 1];
        int year = today.getYear();
        
        return "Jakarta, " + dayName + ", " + day + " " + month + " " + year;
    }
}
