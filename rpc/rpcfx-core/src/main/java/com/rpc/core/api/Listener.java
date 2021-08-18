package com.rpc.core.api;

import java.util.List;

public interface Listener {


  void notify(List<String> updateChildren);
}
