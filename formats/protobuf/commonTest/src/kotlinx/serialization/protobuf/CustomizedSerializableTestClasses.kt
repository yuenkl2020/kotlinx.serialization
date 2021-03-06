/*
 * Copyright 2017-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */
@file:ContextualSerialization(B::class)
package kotlinx.serialization.protobuf

import kotlinx.serialization.*
import kotlinx.serialization.builtins.*

@Serializable
data class A(@ProtoId(1) val b: B)

data class B(@ProtoId(1) val value: Int)

object BSerializer : KSerializer<B> {
    override fun serialize(encoder: Encoder, value: B) {
        encoder.encodeInt(value.value)
    }

    override fun deserialize(decoder: Decoder): B {
        return B(decoder.decodeInt())
    }

    override val descriptor: SerialDescriptor = PrimitiveDescriptor("B", PrimitiveKind.INT)
}

@Serializable
data class BList(@ProtoId(1) val bs: List<B>)

@Serializable
data class C(@ProtoId(1) val a: Int = 31, @ProtoId(2) val b: Int = 42) {
    @Serializer(forClass = C::class)
    companion object: KSerializer<C> {
        override fun serialize(encoder: Encoder, value: C) {
            val elemOutput = encoder.beginStructure(descriptor)
            elemOutput.encodeIntElement(descriptor, 1, value.b)
            if (value.a != 31) elemOutput.encodeIntElement(descriptor, 0, value.a)
            elemOutput.endStructure(descriptor)
        }
    }
}

@Serializable
data class CList1(@ProtoId(1) val c: List<C>)

@Serializable
data class CList2(@ProtoId(1) val d: Int = 5, @ProtoId(2) val c: List<C>) {
    @Serializer(forClass = CList2::class)
    companion object: KSerializer<CList2> {
        override fun serialize(encoder: Encoder, value: CList2) {
            val elemOutput = encoder.beginStructure(descriptor)
            elemOutput.encodeSerializableElement(descriptor, 1, C.list, value.c)
            if (value.d != 5) elemOutput.encodeIntElement(descriptor, 0, value.d)
            elemOutput.endStructure(descriptor)
        }
    }
}

@Serializable
data class CList3(@ProtoId(1) val e: List<C> = emptyList(), @ProtoId(2) val f: Int) {
    @Serializer(forClass = CList3::class)
    companion object: KSerializer<CList3> {
        override fun serialize(encoder: Encoder, value: CList3) {
            val elemOutput = encoder.beginStructure(descriptor)
            if (value.e.isNotEmpty()) elemOutput.encodeSerializableElement(descriptor, 0, C.list, value.e)
            elemOutput.encodeIntElement(descriptor, 1, value.f)
            elemOutput.endStructure(descriptor)
        }
    }
}

@Serializable
data class CList4(@ProtoId(1) val g: List<C> = emptyList(), @ProtoId(2) val h: Int) {
    @Serializer(forClass = CList4::class)
    companion object: KSerializer<CList4> {
        override fun serialize(encoder: Encoder, value: CList4) {
            val elemOutput = encoder.beginStructure(descriptor)
            elemOutput.encodeIntElement(descriptor, 1, value.h)
            if (value.g.isNotEmpty()) elemOutput.encodeSerializableElement(descriptor, 0, C.list, value.g)
            elemOutput.endStructure(descriptor)
        }
    }
}

@Serializable
data class CList5(@ProtoId(1) val g: List<Int> = emptyList(), @ProtoId(2) val h: Int) {
    @Serializer(forClass = CList5::class)
    companion object: KSerializer<CList5> {
        override fun serialize(encoder: Encoder, value: CList5) {
            val elemOutput = encoder.beginStructure(descriptor)
            elemOutput.encodeIntElement(descriptor, 1, value.h)
            if (value.g.isNotEmpty()) elemOutput.encodeSerializableElement(descriptor, 0, Int.serializer().list,
                value.g)
            elemOutput.endStructure(descriptor)
        }
    }
}
