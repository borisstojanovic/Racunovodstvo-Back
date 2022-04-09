package rs.raf.demo.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.repositories.KontnaGrupaRepository;
import rs.raf.demo.repositories.KontoRepository;
import rs.raf.demo.requests.BilansDateRequest;
import rs.raf.demo.requests.BilansRequest;
import rs.raf.demo.responses.BilansResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BilansService {

    private final KontnaGrupaRepository kontnaGrupaRepository;
    private final KontoRepository kontoRepository;

    @Autowired
    public BilansService(KontnaGrupaRepository kontnaGrupaRepository, KontoRepository kontoRepository) {
        this.kontnaGrupaRepository = kontnaGrupaRepository;
        this.kontoRepository = kontoRepository;
    }

    public List<BilansResponse> findBilansStanja(List<BilansDateRequest> datumi) {
        List<BilansResponse> threeChars = kontoRepository.findBilansStanjaThirdGroup();
        List<BilansResponse> twoChars = kontoRepository.findBilansStanjaSecondGroup();
        List<BilansResponse> oneChar = kontoRepository.findBilansStanjaFirstGroup();
        List<BilansResponse> bilansStanja = new ArrayList<>();
        for (BilansDateRequest bd : datumi) {
            bilansStanja.addAll(kontoRepository.findAllBilansStanja(bd.getDatumOd(), bd.getDatumDo()));
        }
        bilansStanja = sumValues(bilansStanja);
        return sortBilans(sumGroupedBilans(bilansStanja, threeChars, twoChars, oneChar));
    }

    public List<BilansResponse> findBilansUspeha(List<BilansDateRequest> datumi) {
        List<BilansResponse> threeChars = kontoRepository.findBilansUspehaThirdGroup();
        List<BilansResponse> twoChars = kontoRepository.findBilansUspehaSecondGroup();
        List<BilansResponse> oneChar = kontoRepository.findBilansUspehaFirstGroup();
        List<BilansResponse> bilansUspeha = new ArrayList<>();
        for (BilansDateRequest bd : datumi) {
            bilansUspeha.addAll(kontoRepository.findAllBilansUspeha(bd.getDatumOd(), bd.getDatumDo()));
        }
        bilansUspeha = sumValues(bilansUspeha);
        return sortBilans(sumGroupedBilans(bilansUspeha, threeChars, twoChars, oneChar));
    }

    public List<BilansResponse> findBrutoBilans(String brojKontaOd, String brojKontaDo, Date datumOd, Date datumDo) {
        List<BilansResponse> bilansList = kontnaGrupaRepository.findAllForBilans(brojKontaOd, brojKontaDo, datumOd, datumDo);
        bilansList.sort(Comparator.comparing(BilansResponse::getBrojKonta).reversed());
        Map<String, Double> dugujeMap = new HashMap<>();
        Map<String, Double> potrazujeMap = new HashMap<>();
        System.out.println(bilansList);
        bilansList.forEach(bilansResponse -> {
            if (bilansResponse.getBrojKonta().length() > 3) {
                String key = bilansResponse.getBrojKonta().substring(0, 3);
                dugujeMap.put(key, dugujeMap.getOrDefault(key, 0.0) + bilansResponse.getDuguje());
                potrazujeMap.put(key, potrazujeMap.getOrDefault(key, 0.0) + bilansResponse.getPotrazuje());
            } else if (bilansResponse.getBrojKonta().length() <= 3) {
                String key = bilansResponse.getBrojKonta();
                System.out.println(key);
                bilansResponse.setDuguje(bilansResponse.getDuguje() + dugujeMap.getOrDefault(key, 0.0));
                bilansResponse.setPotrazuje(bilansResponse.getPotrazuje() + potrazujeMap.getOrDefault(key, 0.0));
                bilansResponse.setSaldo(bilansResponse.getDuguje() - bilansResponse.getPotrazuje());
                System.out.println(bilansResponse);
                if (key.length() > 1) {
                    key = key.substring(0, key.length() - 1);
                    dugujeMap.put(key, dugujeMap.getOrDefault(key, 0.0) + bilansResponse.getDuguje());
                    potrazujeMap.put(key, potrazujeMap.getOrDefault(key, 0.0) + bilansResponse.getPotrazuje());
                }
            }
        });
        System.out.println(bilansList);
        return bilansList;
    }

    private List<BilansResponse> getBilans(Date datumOd, Date datumDo) {

        List<BilansResponse> threeChars = kontoRepository.findThirdGroup();
        List<BilansResponse> twoChars = kontoRepository.findSecondGroup();
        List<BilansResponse> oneChar = kontoRepository.findFirstGroup();
        List<BilansResponse> mandatory = kontoRepository.findOtherGroups(datumOd, datumDo);

        return sortBilans(sumGroupedBilans(mandatory, threeChars, twoChars, oneChar));
    }

    private List<BilansResponse> sortBilans(List<BilansResponse> bilansResponses) {
        bilansResponses.sort(Comparator.comparing(BilansResponse::getBrojKonta));
        return bilansResponses;
    }

    private List<BilansResponse> sumValues(List<BilansResponse> bilansResponses) {

        Map<String, BilansResponse> sum = new HashMap<>();

        for (BilansResponse b : bilansResponses) {
            if (!sum.containsKey(b.getBrojKonta())) {
                sum.put(b.getBrojKonta(), b);
            } else {
                sum.put(b.getBrojKonta(), b);
            }
        }
        List<BilansResponse> summedValues = sum.values().stream().collect(Collectors.toList());

        return summedValues;
    }

    private List<BilansResponse> sumGroupedBilans(List<BilansResponse> bilansResponses,
                                                  List<BilansResponse> threeChars,
                                                  List<BilansResponse> twoChars,
                                                  List<BilansResponse> oneChar) {

        List<BilansResponse> mandatory = bilansResponses;

        for (BilansResponse b : bilansResponses) {
            for (BilansResponse br : threeChars) {
                if (b.getBrojKonta().startsWith(br.getBrojKonta())) {
                    br.setPotrazuje(br.getPotrazuje() + b.getPotrazuje());
                    br.setBrojStavki(br.getBrojStavki() + b.getBrojStavki());
                    br.setDuguje(br.getDuguje() + b.getDuguje());
                }
            }
        }
        for (BilansResponse b : threeChars) {
            for (BilansResponse br : twoChars) {
                if (b.getBrojKonta().startsWith(br.getBrojKonta())) {
                    br.setPotrazuje(br.getPotrazuje() + b.getPotrazuje());
                    br.setDuguje(br.getDuguje() + b.getDuguje());
                    br.setBrojStavki(br.getBrojStavki() + b.getBrojStavki());
                    b.setSaldo(b.getDuguje() - b.getPotrazuje());
                }
            }
        }
        for (BilansResponse b : twoChars) {
            for (BilansResponse br : oneChar) {
                if (b.getBrojKonta().startsWith(br.getBrojKonta())) {
                    br.setPotrazuje(br.getPotrazuje() + b.getPotrazuje());
                    br.setDuguje(br.getDuguje() + b.getDuguje());
                    br.setBrojStavki(br.getBrojStavki() + b.getBrojStavki());
                    b.setSaldo(b.getDuguje() - b.getPotrazuje());
                }
            }
        }
        for (BilansResponse b : oneChar) {
            b.setSaldo(b.getDuguje() - b.getPotrazuje());
        }

        mandatory.addAll(threeChars);
        mandatory.addAll(twoChars);
        mandatory.addAll(oneChar);
        return mandatory;
    }
}

