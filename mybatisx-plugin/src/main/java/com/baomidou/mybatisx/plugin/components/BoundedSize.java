//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.baomidou.mybatisx.plugin.components;

import lombok.Getter;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

@Getter
public final class BoundedSize implements Size, Serializable {
  private final Size basis;
  private Size lowerBound;
  private Size upperBound;

  public BoundedSize(Size basis, Size lowerBound, Size upperBound) {
    if (basis == null) {
      throw new NullPointerException("The basis of a bounded size must not be null.");
    } else {
      this.basis = basis;
      this.lowerBound = lowerBound;
      this.upperBound = upperBound;
    }
  }

  public int maximumSize(Container container, List components, FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure, FormLayout.Measure defaultMeasure) {
    int size = this.basis.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure);
    if (this.lowerBound != null) {
      size = Math.max(size, this.lowerBound.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure));
    }

    if (this.upperBound != null) {
      size = Math.min(size, this.upperBound.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure));
    }

    return size;
  }

  public boolean compressible() {
    return this.getBasis().compressible();
  }

  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (!(object instanceof BoundedSize)) {
      return false;
    } else {
      BoundedSize size = (BoundedSize) object;
      return this.basis.equals(size.basis) && (this.lowerBound == null && size.lowerBound == null || this.lowerBound != null && this.lowerBound.equals(size.lowerBound)) && (this.upperBound == null && size.upperBound == null || this.upperBound != null && this.upperBound.equals(size.upperBound));
    }
  }

  @Override
  public int hashCode() {
    int hashValue = this.basis.hashCode();
    if (this.lowerBound != null) {
      hashValue = hashValue * 37 + this.lowerBound.hashCode();
    }

    if (this.upperBound != null) {
      hashValue = hashValue * 37 + this.upperBound.hashCode();
    }

    return hashValue;
  }

  @Override
  public String toString() {
    if (this.lowerBound != null) {
      return this.upperBound == null ? "max(" + this.basis + ';' + this.lowerBound + ')' : "max(" + this.lowerBound + ';' + "min(" + this.basis + ';' + this.upperBound + "))";
    } else {
      return this.upperBound != null ? "min(" + this.basis + ';' + this.upperBound + ')' : "bounded(" + this.basis + ')';
    }
  }
}
