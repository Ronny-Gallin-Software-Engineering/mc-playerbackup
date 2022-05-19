package de.rgse.mc.playerbackup.permissions;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Optional;

public class PermissionTypeAdapter extends TypeAdapter<Permissions.Permission> {

    @Override
    public void write(JsonWriter out, Permissions.Permission value) throws IOException {
        out.beginObject();

        out.name("permissionId").value(value.getPermissionId());
        out.name("permissionLevel").value(value.getPermissionLevel());
        out.name("description").value(value.getDescription());

        out.endObject();
    }

    @Override
    public Permissions.Permission read(JsonReader in) throws IOException {
        Permissions.Permission permission = new Permissions.Permission();

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();

            switch (name) {
                case "permissionId":
                    permission.setPermissionId(in.nextString());
                    break;
                case "permissionLevel":
                    JsonToken peek = in.peek();
                    int level = 0;
                    if (peek == JsonToken.STRING) {
                        String value = in.nextString();
                        Optional<PermissionLevel> pl = PermissionLevel.of(value);
                        level = pl.orElse(PermissionLevel.DEFAULT_PLAYER).getLevel();
                    } else if (peek == JsonToken.NUMBER) {
                        level = in.nextInt();
                    } else {
                        in.skipValue();
                    }
                    permission.setPermissionLevel(level);
                    break;
                case "description":
                    permission.setDescription(in.nextString());
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        return permission;
    }
}
