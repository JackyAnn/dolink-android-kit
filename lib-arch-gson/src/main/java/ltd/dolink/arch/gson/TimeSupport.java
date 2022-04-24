package ltd.dolink.arch.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Objects;

public class TimeSupport {
    public static final TypeAdapterFactory TIME_SUPPORT_FACTORY = new TypeAdapterFactory() {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Class<? super T> rawType = typeToken.getRawType();
            if (Duration.class.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) (new DurationTypeAdapter());
            } else if (Instant.class.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) (new InstantTypeAdapter());
            } else if (LocalDate.class.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) (new LocalDateTypeAdapter());
            } else if (LocalDateTime.class.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) (new LocalDateTimeTypeAdapter());
            } else if (LocalTime.class.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) (new LocalTimeTypeAdapter());
            } else if (OffsetDateTime.class.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) (new OffsetDateTimeTypeAdapter());
            } else if (OffsetDateTime.class.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) (new OffsetTimeTypeAdapter());
            } else if (ZonedDateTime.class.isAssignableFrom(rawType)) {
                return (TypeAdapter<T>) (new ZonedDateTimeTypeAdapter());
            }
            return null;
        }
    };

    public static final GsonBuilder registerTypeAdapter(GsonBuilder builder) {
        if (Objects.isNull(builder)) {
            return builder;
        }
        builder.registerTypeAdapterFactory(TIME_SUPPORT_FACTORY);
        return builder;
    }

    public static class DurationTypeAdapter extends TypeAdapter<Duration> {

        @Override
        public void write(JsonWriter out, Duration value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toString());
            }
        }

        @Override
        public Duration read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return Duration.parse(in.nextString());
        }
    }

    public static class InstantTypeAdapter extends TypeAdapter<Instant> {

        @Override
        public void write(JsonWriter out, Instant value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toString());
            }
        }

        @Override
        public Instant read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return Instant.parse(in.nextString());
        }
    }

    public static class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toString());
            }
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return LocalDate.parse(in.nextString());
        }
    }


    public static class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {

        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toString());
            }
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return LocalDateTime.parse(in.nextString());
        }
    }

    public static class LocalTimeTypeAdapter extends TypeAdapter<LocalTime> {

        @Override
        public void write(JsonWriter out, LocalTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toString());
            }
        }

        @Override
        public LocalTime read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return LocalTime.parse(in.nextString());
        }

    }

    public static class OffsetDateTimeTypeAdapter extends TypeAdapter<OffsetDateTime> {

        @Override
        public void write(JsonWriter out, OffsetDateTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toString());
            }
        }

        @Override
        public OffsetDateTime read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return OffsetDateTime.parse(in.nextString());
        }

    }

    public static class OffsetTimeTypeAdapter extends TypeAdapter<OffsetTime> {

        @Override
        public void write(JsonWriter out, OffsetTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toString());
            }
        }

        @Override
        public OffsetTime read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return OffsetTime.parse(in.nextString());
        }

    }

    public static class ZonedDateTimeTypeAdapter extends TypeAdapter<ZonedDateTime> {

        @Override
        public void write(JsonWriter out, ZonedDateTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toString());
            }
        }

        @Override
        public ZonedDateTime read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return ZonedDateTime.parse(in.nextString());
        }

    }


}
