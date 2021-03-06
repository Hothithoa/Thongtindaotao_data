package Database;

import Model.AllPoint;
import Model.AllPointId;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HO_THI_THOM
 */
public class getAllPoint {

    public getAllPoint() {
    }

    public static AllPointId getAllPoint(String id) {
        AllPointId stu = new AllPointId();
        AllPoint allPoint;
        List<AllPoint> arrAllPoint;
        try {
            final WebClient webClient = new WebClient();
            final URL url = new URL("http://thongtindaotao.sgu.edu.vn/default.aspx?page=nhapmasv&flag=XemDiemThi");
            // turn off warning html unit
            java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.setCssErrorHandler(new SilentCssErrorHandler());

            try {
                final HtmlPage page = webClient.getPage(url);

                HtmlInput searchMSSV = (HtmlInput) page.getElementById("ctl00_ContentPlaceHolder1_ctl00_txtMaSV");
                searchMSSV.type(id);
                stu.setId(id);

                HtmlInput submitDataMssv = (HtmlInput) page.getElementById("ctl00_ContentPlaceHolder1_ctl00_btnOK");
                HtmlPage inforStudent = submitDataMssv.click();

                DomNodeList<DomNode> listDanhSachDiem = inforStudent.querySelectorAll(".row-diem .Label ");

                int mamon = 1;
                int tenmon = 2;
                int diemhe4 = 12;
                int diem10 = 9;
                int loai = 11;

                int i = 0;
                arrAllPoint = new ArrayList<AllPoint>();
                while (i < listDanhSachDiem.getLength()) {
                    allPoint = new AllPoint();
                    allPoint.setMamon(listDanhSachDiem.get(i + mamon).asText());
                    allPoint.setTenmon(listDanhSachDiem.get(i + tenmon).asText());
                    allPoint.setDiem4(listDanhSachDiem.get(i + diemhe4).asText());
                    allPoint.setDiem10(listDanhSachDiem.get(i + diem10).asText());
                    allPoint.setXeploai(listDanhSachDiem.get(i + loai).asText());
                    arrAllPoint.add(allPoint);
                    i += 14;
                }

                stu.setAllPoint(arrAllPoint);

            } catch (IOException ex) {
                Logger.getLogger(getAllPoint.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FailingHttpStatusCodeException ex) {
                Logger.getLogger(getAllPoint.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getAllPoint.class.getName()).log(Level.SEVERE, null, ex);
        }

        return stu;
    }

    public static void getAllPointId(String mssv, String f) {
        AllPointId stu;
        try {
            String line = "";
            File f1 = new File(f);
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(f1, true), StandardCharsets.UTF_8);
            try {
                stu = getAllPoint(mssv);

                for (int i = 0; i < stu.getAllPoint().size(); i++) {
                    String strJson = "{\"id\":\"" + stu.getId() + "\","
                            + "\"mamon\":\"" + stu.getAllPoint().get(i).getMamon() + "\","
                            + "\"tenmon\":\"" + stu.getAllPoint().get(i).getTenmon() + "\","
                            + "\"diem10\":\"" + stu.getAllPoint().get(i).getDiem10() + "\","
                            + "\"diem4\":\"" + stu.getAllPoint().get(i).getDiem4() + "\","
                            + "\"xeploai\":\"" + stu.getAllPoint().get(i).getXeploai() + "\"}";

                    writer.write(strJson);
                    writer.write("\n");
                    writer.flush();
                    System.out.println("id"+stu.getId());
                }
            } catch (IOException ex) {
                System.out.println("errorr");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getAllPointId("3118410419","new_db_json\\TOANUD\\allpoint_toan_ung_dung.json");
    }

}
