package org.javaboy.vhr.utils;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.javaboy.vhr.model.Department;
import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.JobLevel;
import org.javaboy.vhr.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 选手数据 Excel 导入导出工具。
 * 模板已 CS 化：不再包含身份证/婚姻状况/民族/政治面貌等人事字段。
 */
public class POIUtils {
    private static final Logger logger = LoggerFactory.getLogger(POIUtils.class);
    /**
     * Excel 列宽单位（1 个字符宽度对应的 POI 宽度值）
     */
    private static final int COLUMN_WIDTH_UNIT = 256;
    /**
     * 标题行行号（第 1 行，0 基）
     */
    private static final int HEADER_ROW_INDEX = 0;
    private static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
    private static final String DATE_CELL_FORMAT = "m/d/yy";
    private static final String SHEET_NAME = "选手信息表";
    private static final String EXPORT_FILE_NAME = "选手表.xls";
    /**
     * 选手导入模板中每一列对应的字段下标
     */
    private static final int COL_NAME = 1;
    private static final int COL_WORK_ID = 2;
    private static final int COL_GENDER = 3;
    private static final int COL_BIRTHDAY = 4;
    private static final int COL_NATIVE_PLACE = 5;
    private static final int COL_PHONE = 6;
    private static final int COL_ADDRESS = 7;
    private static final int COL_DEPARTMENT = 8;
    private static final int COL_JOB_LEVEL = 9;
    private static final int COL_POSITION = 10;
    private static final int COL_ENGAGE_FORM = 11;
    private static final int COL_TIPTOP_DEGREE = 12;
    private static final int COL_SPECIALTY = 13;
    private static final int COL_SCHOOL = 14;
    private static final int COL_BEGIN_DATE = 15;
    private static final int COL_WORK_STATE = 16;
    private static final int COL_EMAIL = 17;
    private static final int COL_CONTRACT_TERM = 18;
    private static final int COL_BEGIN_CONTRACT = 19;
    private static final int COL_END_CONTRACT = 20;
    private static final int COL_CONVERSION_TIME = 21;

    /**
     * 将选手数据导出为 Excel 文件响应
     *
     * @param list 选手数据集合
     * @return Excel 文件字节流响应
     */
    public static ResponseEntity<byte[]> employee2Excel(List<Employee> list) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("选手信息");
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle(SHEET_NAME);
        //5. 创建样式
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_CELL_FORMAT));
        HSSFSheet sheet = workbook.createSheet(SHEET_NAME);
        //设置列的宽度
        int[] columnWidths = {5, 12, 10, 5, 12, 16, 15, 20, 16, 14, 10,
                12, 10, 20, 20, 15, 8, 25, 14, 15, 15, 15};
        for (int i = 0; i < columnWidths.length; i++) {
            sheet.setColumnWidth(i, columnWidths[i] * COLUMN_WIDTH_UNIT);
        }
        //6. 创建标题行
        String[] headers = {"编号", "姓名", "工号", "性别", "出生日期", "真实姓名",
                "电话号码", "联系地址", "所属战队", "选手等级", "位置", "聘用形式",
                "最高学历", "专业", "毕业院校", "入队日期", "在职状态", "邮箱",
                "合同期限(年)", "合同起始日期", "合同终止日期", "转正日期"};
        HSSFRow r0 = sheet.createRow(HEADER_ROW_INDEX);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = r0.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(headers[i]);
        }
        for (int i = 0; i < list.size(); i++) {
            Employee emp = list.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(emp.getId());
            row.createCell(1).setCellValue(emp.getName());
            row.createCell(2).setCellValue(emp.getWorkID());
            row.createCell(3).setCellValue(emp.getGender());
            HSSFCell cell4 = row.createCell(COL_BIRTHDAY);
            cell4.setCellStyle(dateCellStyle);
            cell4.setCellValue(emp.getBirthday());
            row.createCell(COL_NATIVE_PLACE).setCellValue(emp.getNativePlace());
            row.createCell(COL_PHONE).setCellValue(emp.getPhone());
            row.createCell(COL_ADDRESS).setCellValue(emp.getAddress());
            row.createCell(COL_DEPARTMENT).setCellValue(emp.getDepartment() == null ? "" : emp.getDepartment().getName());
            row.createCell(COL_JOB_LEVEL).setCellValue(emp.getJobLevel() == null ? "" : emp.getJobLevel().getName());
            row.createCell(COL_POSITION).setCellValue(emp.getPosition() == null ? "" : emp.getPosition().getName());
            row.createCell(COL_ENGAGE_FORM).setCellValue(emp.getEngageForm());
            row.createCell(COL_TIPTOP_DEGREE).setCellValue(emp.getTiptopDegree());
            row.createCell(COL_SPECIALTY).setCellValue(emp.getSpecialty());
            row.createCell(COL_SCHOOL).setCellValue(emp.getSchool());
            HSSFCell cell15 = row.createCell(COL_BEGIN_DATE);
            cell15.setCellStyle(dateCellStyle);
            cell15.setCellValue(emp.getBeginDate());
            row.createCell(COL_WORK_STATE).setCellValue(emp.getWorkState());
            row.createCell(COL_EMAIL).setCellValue(emp.getEmail());
            row.createCell(COL_CONTRACT_TERM).setCellValue(emp.getContractTerm());
            HSSFCell cell19 = row.createCell(COL_BEGIN_CONTRACT);
            cell19.setCellStyle(dateCellStyle);
            cell19.setCellValue(emp.getBeginContract());
            HSSFCell cell20 = row.createCell(COL_END_CONTRACT);
            cell20.setCellStyle(dateCellStyle);
            cell20.setCellValue(emp.getEndContract());
            HSSFCell cell21 = row.createCell(COL_CONVERSION_TIME);
            cell21.setCellStyle(dateCellStyle);
            cell21.setCellValue(emp.getConversionTime());
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            HttpHeaders headers2 = new HttpHeaders();
            //部分浏览器（老版本 IE 等）对非 ASCII 文件名按 ISO-8859-1 解码，这里做一次编码转换
            String encodedFileName = new String(EXPORT_FILE_NAME.getBytes(StandardCharsets.UTF_8),
                    Charset.forName(CHARSET_ISO_8859_1));
            headers2.setContentDispositionFormData("attachment", encodedFileName);
            headers2.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
            return new ResponseEntity<>(baos.toByteArray(), headers2, HttpStatus.CREATED);
        } catch (IOException e) {
            logger.error("选手数据导出 Excel 失败", e);
            return new ResponseEntity<>(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Excel 解析成选手数据集合
     *
     * @param file             上传的 Excel 文件
     * @param allDepartments   所有战队
     * @param allPositions     所有位置
     * @param allJobLevels     所有选手等级
     * @return 解析出的选手数据集合
     */
    public static List<Employee> excel2Employee(MultipartFile file,
                                                List<Department> allDepartments,
                                                List<Position> allPositions,
                                                List<JobLevel> allJobLevels) {
        List<Employee> list = new ArrayList<>();
        try (HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream())) {
            //1. 获取 workbook 中表单的数量
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                //2. 获取表单
                HSSFSheet sheet = workbook.getSheetAt(i);
                //3. 获取表单中的行数
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j < physicalNumberOfRows; j++) {
                    //4. 跳过标题行
                    if (j == HEADER_ROW_INDEX) {
                        continue;
                    }
                    //5. 获取行
                    HSSFRow row = sheet.getRow(j);
                    if (row == null) {
                        continue;//防止数据中间有空行
                    }
                    //6. 获取列数
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    Employee employee = new Employee();
                    for (int k = 0; k < physicalNumberOfCells; k++) {
                        HSSFCell cell = row.getCell(k);
                        if (cell == null) {
                            continue;
                        }
                        parseCell(employee, cell, k,
                                allDepartments, allPositions, allJobLevels);
                    }
                    list.add(employee);
                }
            }
        } catch (IOException e) {
            logger.error("Excel 解析为选手数据失败", e);
        }
        return list;
    }

    /**
     * 解析单个单元格并填充到选手对象
     */
    private static void parseCell(Employee employee, HSSFCell cell, int k,
                                  List<Department> allDepartments,
                                  List<Position> allPositions, List<JobLevel> allJobLevels) {
        if (cell.getCellType() == org.apache.poi.ss.usermodel.CellType.STRING) {
            String cellValue = cell.getStringCellValue();
            if (!StringUtils.hasText(cellValue)) {
                return;
            }
            switch (k) {
                case COL_NAME:
                    employee.setName(cellValue);
                    break;
                case COL_WORK_ID:
                    employee.setWorkID(cellValue);
                    break;
                case COL_GENDER:
                    employee.setGender(cellValue);
                    break;
                case COL_NATIVE_PLACE:
                    employee.setNativePlace(cellValue);
                    break;
                case COL_PHONE:
                    employee.setPhone(cellValue);
                    break;
                case COL_ADDRESS:
                    employee.setAddress(cellValue);
                    break;
                case COL_DEPARTMENT:
                    int departmentIndex = allDepartments.indexOf(new Department(cellValue));
                    if (departmentIndex >= 0) {
                        employee.setDepartmentId(allDepartments.get(departmentIndex).getId());
                    }
                    break;
                case COL_JOB_LEVEL:
                    int jobLevelIndex = allJobLevels.indexOf(new JobLevel(cellValue));
                    if (jobLevelIndex >= 0) {
                        employee.setJobLevelId(allJobLevels.get(jobLevelIndex).getId());
                    }
                    break;
                case COL_POSITION:
                    int positionIndex = allPositions.indexOf(new Position(cellValue));
                    if (positionIndex >= 0) {
                        employee.setPosId(allPositions.get(positionIndex).getId());
                    }
                    break;
                case COL_ENGAGE_FORM:
                    employee.setEngageForm(cellValue);
                    break;
                case COL_TIPTOP_DEGREE:
                    employee.setTiptopDegree(cellValue);
                    break;
                case COL_SPECIALTY:
                    employee.setSpecialty(cellValue);
                    break;
                case COL_SCHOOL:
                    employee.setSchool(cellValue);
                    break;
                case COL_WORK_STATE:
                    employee.setWorkState(cellValue);
                    break;
                case COL_EMAIL:
                    employee.setEmail(cellValue);
                    break;
                default:
                    break;
            }
        } else {
            switch (k) {
                case COL_BIRTHDAY:
                    employee.setBirthday(cell.getDateCellValue());
                    break;
                case COL_BEGIN_DATE:
                    employee.setBeginDate(cell.getDateCellValue());
                    break;
                case COL_BEGIN_CONTRACT:
                    employee.setBeginContract(cell.getDateCellValue());
                    break;
                case COL_END_CONTRACT:
                    employee.setEndContract(cell.getDateCellValue());
                    break;
                case COL_CONTRACT_TERM:
                    employee.setContractTerm(cell.getNumericCellValue());
                    break;
                case COL_CONVERSION_TIME:
                    employee.setConversionTime(cell.getDateCellValue());
                    break;
                default:
                    break;
            }
        }
    }
}
