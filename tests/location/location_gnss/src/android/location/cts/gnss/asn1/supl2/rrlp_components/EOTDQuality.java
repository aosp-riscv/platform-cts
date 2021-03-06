/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.location.cts.asn1.supl2.rrlp_components;

/*
 */


//
//
import android.location.cts.asn1.base.Asn1Integer;
import android.location.cts.asn1.base.Asn1Null;
import android.location.cts.asn1.base.Asn1Object;
import android.location.cts.asn1.base.Asn1Sequence;
import android.location.cts.asn1.base.Asn1Tag;
import android.location.cts.asn1.base.BitStream;
import android.location.cts.asn1.base.BitStreamReader;
import android.location.cts.asn1.base.SequenceComponent;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import javax.annotation.Nullable;


/**
*/
public  class EOTDQuality extends Asn1Sequence {
  //

  private static final Asn1Tag TAG_EOTDQuality
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public EOTDQuality() {
    super();
  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_EOTDQuality;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_EOTDQuality != null) {
      return ImmutableList.of(TAG_EOTDQuality);
    } else {
      return Asn1Sequence.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new EOTDQuality from encoded stream.
   */
  public static EOTDQuality fromPerUnaligned(byte[] encodedBytes) {
    EOTDQuality result = new EOTDQuality();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new EOTDQuality from encoded stream.
   */
  public static EOTDQuality fromPerAligned(byte[] encodedBytes) {
    EOTDQuality result = new EOTDQuality();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }



  @Override protected boolean isExtensible() {
    return false;
  }

  @Override public boolean containsExtensionValues() {
    for (SequenceComponent extensionComponent : getExtensionComponents()) {
      if (extensionComponent.isExplicitlySet()) return true;
    }
    return false;
  }

  
  private EOTDQuality.nbrOfMeasurementsType nbrOfMeasurements_;
  public EOTDQuality.nbrOfMeasurementsType getNbrOfMeasurements() {
    return nbrOfMeasurements_;
  }
  /**
   * @throws ClassCastException if value is not a EOTDQuality.nbrOfMeasurementsType
   */
  public void setNbrOfMeasurements(Asn1Object value) {
    this.nbrOfMeasurements_ = (EOTDQuality.nbrOfMeasurementsType) value;
  }
  public EOTDQuality.nbrOfMeasurementsType setNbrOfMeasurementsToNewInstance() {
    nbrOfMeasurements_ = new EOTDQuality.nbrOfMeasurementsType();
    return nbrOfMeasurements_;
  }
  
  private EOTDQuality.stdOfEOTDType stdOfEOTD_;
  public EOTDQuality.stdOfEOTDType getStdOfEOTD() {
    return stdOfEOTD_;
  }
  /**
   * @throws ClassCastException if value is not a EOTDQuality.stdOfEOTDType
   */
  public void setStdOfEOTD(Asn1Object value) {
    this.stdOfEOTD_ = (EOTDQuality.stdOfEOTDType) value;
  }
  public EOTDQuality.stdOfEOTDType setStdOfEOTDToNewInstance() {
    stdOfEOTD_ = new EOTDQuality.stdOfEOTDType();
    return stdOfEOTD_;
  }
  

  

  

  @Override public Iterable<? extends SequenceComponent> getComponents() {
    ImmutableList.Builder<SequenceComponent> builder = ImmutableList.builder();
    
    builder.add(new SequenceComponent() {
          Asn1Tag tag = Asn1Tag.fromClassAndNumber(2, 0);

          @Override public boolean isExplicitlySet() {
            return getNbrOfMeasurements() != null;
          }

          @Override public boolean hasDefaultValue() {
            return false;
          }

          @Override public boolean isOptional() {
            return false;
          }

          @Override public Asn1Object getComponentValue() {
            return getNbrOfMeasurements();
          }

          @Override public void setToNewInstance() {
            setNbrOfMeasurementsToNewInstance();
          }

          @Override public Collection<Asn1Tag> getPossibleFirstTags() {
            return tag == null ? EOTDQuality.nbrOfMeasurementsType.getPossibleFirstTags() : ImmutableList.of(tag);
          }

          @Override
          public Asn1Tag getTag() {
            return tag;
          }

          @Override
          public boolean isImplicitTagging() {
            return true;
          }

          @Override public String toIndentedString(String indent) {
                return "nbrOfMeasurements : "
                    + getNbrOfMeasurements().toIndentedString(indent);
              }
        });
    
    builder.add(new SequenceComponent() {
          Asn1Tag tag = Asn1Tag.fromClassAndNumber(2, 1);

          @Override public boolean isExplicitlySet() {
            return getStdOfEOTD() != null;
          }

          @Override public boolean hasDefaultValue() {
            return false;
          }

          @Override public boolean isOptional() {
            return false;
          }

          @Override public Asn1Object getComponentValue() {
            return getStdOfEOTD();
          }

          @Override public void setToNewInstance() {
            setStdOfEOTDToNewInstance();
          }

          @Override public Collection<Asn1Tag> getPossibleFirstTags() {
            return tag == null ? EOTDQuality.stdOfEOTDType.getPossibleFirstTags() : ImmutableList.of(tag);
          }

          @Override
          public Asn1Tag getTag() {
            return tag;
          }

          @Override
          public boolean isImplicitTagging() {
            return true;
          }

          @Override public String toIndentedString(String indent) {
                return "stdOfEOTD : "
                    + getStdOfEOTD().toIndentedString(indent);
              }
        });
    
    return builder.build();
  }

  @Override public Iterable<? extends SequenceComponent>
                                                    getExtensionComponents() {
    ImmutableList.Builder<SequenceComponent> builder = ImmutableList.builder();
      
      return builder.build();
    }

  
/*
 */


//

/**
 */
public static class nbrOfMeasurementsType extends Asn1Integer {
  //

  private static final Asn1Tag TAG_nbrOfMeasurementsType
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public nbrOfMeasurementsType() {
    super();
    setValueRange("0", "7");

  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_nbrOfMeasurementsType;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_nbrOfMeasurementsType != null) {
      return ImmutableList.of(TAG_nbrOfMeasurementsType);
    } else {
      return Asn1Integer.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new nbrOfMeasurementsType from encoded stream.
   */
  public static nbrOfMeasurementsType fromPerUnaligned(byte[] encodedBytes) {
    nbrOfMeasurementsType result = new nbrOfMeasurementsType();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new nbrOfMeasurementsType from encoded stream.
   */
  public static nbrOfMeasurementsType fromPerAligned(byte[] encodedBytes) {
    nbrOfMeasurementsType result = new nbrOfMeasurementsType();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }

  @Override public Iterable<BitStream> encodePerUnaligned() {
    return super.encodePerUnaligned();
  }

  @Override public Iterable<BitStream> encodePerAligned() {
    return super.encodePerAligned();
  }

  @Override public void decodePerUnaligned(BitStreamReader reader) {
    super.decodePerUnaligned(reader);
  }

  @Override public void decodePerAligned(BitStreamReader reader) {
    super.decodePerAligned(reader);
  }

  @Override public String toString() {
    return toIndentedString("");
  }

  public String toIndentedString(String indent) {
    return "nbrOfMeasurementsType = " + getInteger() + ";\n";
  }
}

  
/*
 */


//

/**
 */
public static class stdOfEOTDType extends Asn1Integer {
  //

  private static final Asn1Tag TAG_stdOfEOTDType
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public stdOfEOTDType() {
    super();
    setValueRange("0", "31");

  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_stdOfEOTDType;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_stdOfEOTDType != null) {
      return ImmutableList.of(TAG_stdOfEOTDType);
    } else {
      return Asn1Integer.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new stdOfEOTDType from encoded stream.
   */
  public static stdOfEOTDType fromPerUnaligned(byte[] encodedBytes) {
    stdOfEOTDType result = new stdOfEOTDType();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new stdOfEOTDType from encoded stream.
   */
  public static stdOfEOTDType fromPerAligned(byte[] encodedBytes) {
    stdOfEOTDType result = new stdOfEOTDType();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }

  @Override public Iterable<BitStream> encodePerUnaligned() {
    return super.encodePerUnaligned();
  }

  @Override public Iterable<BitStream> encodePerAligned() {
    return super.encodePerAligned();
  }

  @Override public void decodePerUnaligned(BitStreamReader reader) {
    super.decodePerUnaligned(reader);
  }

  @Override public void decodePerAligned(BitStreamReader reader) {
    super.decodePerAligned(reader);
  }

  @Override public String toString() {
    return toIndentedString("");
  }

  public String toIndentedString(String indent) {
    return "stdOfEOTDType = " + getInteger() + ";\n";
  }
}

  

    

  @Override public Iterable<BitStream> encodePerUnaligned() {
    return super.encodePerUnaligned();
  }

  @Override public Iterable<BitStream> encodePerAligned() {
    return super.encodePerAligned();
  }

  @Override public void decodePerUnaligned(BitStreamReader reader) {
    super.decodePerUnaligned(reader);
  }

  @Override public void decodePerAligned(BitStreamReader reader) {
    super.decodePerAligned(reader);
  }

  @Override public String toString() {
    return toIndentedString("");
  }

  public String toIndentedString(String indent) {
    StringBuilder builder = new StringBuilder();
    builder.append("EOTDQuality = {\n");
    final String internalIndent = indent + "  ";
    for (SequenceComponent component : getComponents()) {
      if (component.isExplicitlySet()) {
        builder.append(internalIndent)
            .append(component.toIndentedString(internalIndent));
      }
    }
    if (isExtensible()) {
      builder.append(internalIndent).append("...\n");
      for (SequenceComponent component : getExtensionComponents()) {
        if (component.isExplicitlySet()) {
          builder.append(internalIndent)
              .append(component.toIndentedString(internalIndent));
        }
      }
    }
    builder.append(indent).append("};\n");
    return builder.toString();
  }
}
