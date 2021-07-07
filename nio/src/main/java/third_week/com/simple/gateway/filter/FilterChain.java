package third_week.com.simple.gateway.filter;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FilterChain implements Filter {
    private final List<Filter> filterList = new ArrayList<>();

    public void addFilters(List<Filter> filters) {
        filterList.addAll(filters);
    }
    public void addFilters(Filter... filters){
        filterList.addAll(Arrays.asList(filters));
    }
    @Override
    public void preInvoke(FullHttpRequest request) {
        filterList.sort((o1, o2) -> o2.getOrder().compareTo(o1.getOrder()));
        for (Filter filter : filterList) {
            filter.preInvoke(request);
        }
    }

    @Override
    public void onResponse(FullHttpRequest request, FullHttpResponse response) {
        filterList.sort(Comparator.comparing(Filter::getOrder));
        for (Filter filter : filterList) {
            filter.onResponse(request,response);
        }
    }

    @Override
    public Integer getOrder() {
        return null;
    }

}
