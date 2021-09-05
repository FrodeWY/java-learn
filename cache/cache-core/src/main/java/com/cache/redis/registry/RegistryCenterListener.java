package com.cache.redis.registry;

import java.util.List;

public interface RegistryCenterListener {

  void notify(List<String> updateChildren);
}
