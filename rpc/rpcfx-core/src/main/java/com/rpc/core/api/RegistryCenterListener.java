package com.rpc.core.api;

import java.util.List;

public interface RegistryCenterListener {


  void notify(List<String> updateChildren);
}
