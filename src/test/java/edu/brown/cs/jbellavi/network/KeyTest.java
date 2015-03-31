package edu.brown.cs.jbellavi.network;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class KeyTest {
  @Before
  public void setUpTest() {
    Key.deprecate();
  }

  @Test
  public void getKeyTest() throws DeprecatedKeyException {
    Key k = Key.getKey(1000);
    assertNotNull(k);
    assertEquals(1000, k.getId());

    Key k2 = Key.getKey(1000);
    assertNull(k2);

    Key k3 = Key.getKey(2000);
    assertNotNull(k3);
    assertEquals(2000, k3.getId());
    assertEquals(1000, k.getId());
  }

  private static final int MILLION = 1000000;

  @Test
  public void generateTest() throws DeprecatedKeyException {
    Set<Integer> ids = new HashSet<>();
    for (int i = 0; i < MILLION; i++) {
      ids.add(Key.generate().getId());
    }

    assertEquals(MILLION, ids.size());

    for (int i : ids) {
      assertNull(Key.getKey(i));
    }
  }

  @Test
  public void deprecateTest() throws DeprecatedKeyException {
    Key k = Key.generate();
    assertTrue(k.isActive());
    try {
      k.getId();
    } catch (DeprecatedKeyException ex) {
      fail();
    }
    assertTrue(k.equals(k));
    assertFalse(-1 == k.hashCode());
    assertFalse("(Deprecated)".equals(k.toString()));

    Key.deprecate();
    assertFalse(k.isActive());
    try {
      k.getId();
      fail();
    } catch (DeprecatedKeyException ex) {
      assertTrue(true);
    }
    assertFalse(k.equals(k));
    assertEquals(-1, k.hashCode());
    assertEquals("(Deprecated)", k.toString());

    k = Key.generate();
    assertTrue(k.isActive());
    try {
      k.getId();
    } catch (DeprecatedKeyException ex) {
      fail();
    }
    assertTrue(k.equals(k));
    assertFalse(-1 == k.hashCode());
    assertFalse("(Deprecated)".equals(k.toString()));
  }

  @Test
  public void getCopyTest() throws DeprecatedKeyException {
    Key k1 = Key.getKey(1000);
    Key k2 = Key.getCopy(1000);
    assertEquals(k1, k2);

    Key k3 = Key.getCopy(2000);
    Key k4 = Key.getKey(2000);
    assertNull(k4);
    Key k5 = Key.getCopy(2000);
    assertEquals(k3, k5);

    Key k6 = Key.getCopy(k5);
    assertEquals(k3, k6);
    assertEquals(k5, k6);
  }

  @Test
  public void setActiveGenerationTest() throws DeprecatedKeyException {
    Key.setActiveGeneration(15);

    Key k = Key.generate();
    assertTrue(k.isActive());
    try {
      k.getId();
    } catch (DeprecatedKeyException ex) {
      fail();
    }
    assertTrue(k.equals(k));
    assertFalse(-1 == k.hashCode());
    assertFalse("(Deprecated)".equals(k.toString()));

    Key.setActiveGeneration(25);
    assertFalse(k.isActive());
    try {
      k.getId();
      fail();
    } catch (DeprecatedKeyException ex) {
      assertTrue(true);
    }
    assertFalse(k.equals(k));
    assertEquals(-1, k.hashCode());
    assertEquals("(Deprecated)", k.toString());

    k = Key.generate();
    assertTrue(k.isActive());
    try {
      k.getId();
    } catch (DeprecatedKeyException ex) {
      fail();
    }
    assertTrue(k.equals(k));
    assertFalse(-1 == k.hashCode());
    assertFalse("(Deprecated)".equals(k.toString()));

    Key.setActiveGeneration(15);

    k = Key.generate();
    assertTrue(k.isActive());
    try {
      k.getId();
    } catch (DeprecatedKeyException ex) {
      fail();
    }
    assertTrue(k.equals(k));
    assertFalse(-1 == k.hashCode());
    assertFalse("(Deprecated)".equals(k.toString()));
  }

  @Test
  public void jSONStringTest() {
    Key k = Key.generate();
    assertEquals(k, Key.fromJSONString(k.toJSONString()));
  }
}
