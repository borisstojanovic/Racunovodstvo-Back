package rs.raf.demo.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.repositories.KontnaGrupaRepository;
import rs.raf.demo.responses.BilansResponse;

import java.util.*;

@Service
public class BilansService {

    private final KontnaGrupaRepository kontnaGrupaRepository;

    @Autowired
    public BilansService(KontnaGrupaRepository kontnaGrupaRepository) {
        this.kontnaGrupaRepository = kontnaGrupaRepository;
    }

    public List<BilansResponse> findBilans(List<String> startsWith, List<Date> datumiOd, List<Date> datumiDo) {
        Set<BilansResponse> bilansSet = new HashSet<>();
        for (int i = 0; i < datumiDo.size() && i < datumiOd.size(); i++) {
            bilansSet.addAll(kontnaGrupaRepository.findAllStartingWith(startsWith, datumiOd.get(i), datumiDo.get(i)));
        }
        List<BilansResponse> bilansList = new ArrayList<>(bilansSet);
        bilansList.sort(Comparator.comparing(BilansResponse::getBrojKonta).reversed());
        sumBilans(bilansList);
        return bilansList;
    }

    public List<BilansResponse> findBrutoBilans(String brojKontaOd, String brojKontaDo, Date datumOd, Date datumDo) {
        List<BilansResponse> bilansList = kontnaGrupaRepository.findAllForBilans(brojKontaOd, brojKontaDo, datumOd, datumDo);
        bilansList.sort(Comparator.comparing(BilansResponse::getBrojKonta).reversed());

        sumBilans(bilansList);
        return bilansList;
    }

    private void sumBilans(List<BilansResponse> bilansList) {
        Map<String, Double> dugujeMap = new HashMap<>();
        Map<String, Double> potrazujeMap = new HashMap<>();

        bilansList.forEach(bilansResponse -> {
            String brojKonta = bilansResponse.getBrojKonta();
            int length = brojKonta.length();
            if (length <= 3) {
                bilansResponse.setDuguje(bilansResponse.getDuguje() + dugujeMap.getOrDefault(brojKonta, 0.0));
                bilansResponse.setPotrazuje(bilansResponse.getPotrazuje() + potrazujeMap.getOrDefault(brojKonta, 0.0));
                bilansResponse.setSaldo(bilansResponse.getDuguje() - bilansResponse.getPotrazuje());
            }

            String key = length > 3 ? brojKonta.substring(0, 3) : brojKonta.substring(0, brojKonta.length() - 1);
            dugujeMap.put(key, dugujeMap.getOrDefault(key, 0.0) + bilansResponse.getDuguje());
            potrazujeMap.put(key, potrazujeMap.getOrDefault(key, 0.0) + bilansResponse.getPotrazuje());
        });
    }
}

