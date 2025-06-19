package bsshelper.localservice.searchcache;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public interface SearchCacheService {
    int MAX_HISTORY_SIZE = 14;
    ConcurrentLinkedDeque<String> searchingCache = new ConcurrentLinkedDeque<>();

    void add(String query);
    List<String> getList();
}
