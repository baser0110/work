package bsshelper.localservice.searchcache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SearchCacheServiceImpl implements SearchCacheService{

    @Override
    public void add(String query) {
        if (query == null || query.isBlank()) return;

        searchingCache.remove(query);
        searchingCache.addFirst(query);

        while (searchingCache.size() > MAX_HISTORY_SIZE) {
            searchingCache.pollLast();
        }
    }

    @Override
    public List<String> getList() {
        return new ArrayList<>(searchingCache);
    }
}
