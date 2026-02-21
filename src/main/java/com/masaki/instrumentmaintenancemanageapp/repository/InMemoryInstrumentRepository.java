package com.masaki.instrumentmaintenancemanageapp.repository;

import com.masaki.instrumentmaintenancemanageapp.domain.Instrument;
import com.masaki.instrumentmaintenancemanageapp.domain.MaintenanceStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



// 削除します。インメモリはもう使用しない為。

//@Repository
//public class InMemoryInstrumentRepository implements InstrumentRepository {
//
//    private final Map<Long, Instrument> store = new HashMap<>();
//
//    public InMemoryInstrumentRepository() {
//        // 仮データ
//        store.put(1L,
//                new Instrument(1L, "Guitar", MaintenanceStatus.NOT_MAINTAINED));
//
//        // 仮データ2
//        store.put(2L,
//                new Instrument(2L, "Piano", MaintenanceStatus.NOT_MAINTAINED));
//
//        store.put(3L,
//                new Instrument(3L, "Tranpet", MaintenanceStatus.NOT_MAINTAINED));
//
//    }
//
//    //全件取得（Service層から呼ばれる想定）
//    public List<Instrument> findAll() {
//        return new ArrayList<>(store.values());
//    }
//
//    // storeから仮データがあるか検索する。（ID指定で1件取得）
//    @Override
//    public Instrument findById(Long id) {
//        return store.get(id);
//    }
//
//    @Override
//    public void save(Instrument instrument) {
//        store.put(instrument.getId(), instrument);
//    }
//}

