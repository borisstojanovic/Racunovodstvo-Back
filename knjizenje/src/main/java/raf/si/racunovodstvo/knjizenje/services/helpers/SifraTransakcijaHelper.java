package raf.si.racunovodstvo.knjizenje.services.helpers;

import raf.si.racunovodstvo.knjizenje.reports.Reports;
import raf.si.racunovodstvo.knjizenje.reports.TableReport;
import raf.si.racunovodstvo.knjizenje.responses.SifraTransakcijeResponse;
import raf.si.racunovodstvo.knjizenje.responses.TransakcijaResponse;

import java.util.ArrayList;
import java.util.List;

public class SifraTransakcijaHelper {

    private static final List<String> HEADER = List.of("Sifra transakcije","Uplata","Isplata","Saldo");

    private String title;
    private List<SifraTransakcijeResponse> sifraTransakcijeResponses;

    public SifraTransakcijaHelper(String title, List<SifraTransakcijeResponse> content) {
        this.title = title;
        this.sifraTransakcijeResponses = content;
    }

    public Reports makeReport() {
        List<List<String>> table = new ArrayList<>();
        for (SifraTransakcijeResponse sifraTransakcije : sifraTransakcijeResponses) {
            List<String> row = new ArrayList<>();
            row.add(sifraTransakcije.getSifra().toString());
            row.add(sifraTransakcije.getUplata().toString());
            row.add(sifraTransakcije.getIsplata().toString());
            row.add(sifraTransakcije.getSaldo().toString());
            table.add(row);
        }
        return new TableReport("author", String.format("Naslov: %s", title), "footer", HEADER, table);
    }
}
