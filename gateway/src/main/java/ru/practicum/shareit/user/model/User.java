package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Validated
public class User {
    private Long id;

    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
