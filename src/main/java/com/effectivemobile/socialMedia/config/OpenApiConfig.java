package com.effectivemobile.socialMedia.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Social Media API",
                version = "1.0.0",
                description = """
                        Тестовое задание Social Media API для компании Effective Mobile.
                        Для доступа к эндпоинтам используется JWT Bearer Token, он выдается после успешной аутетификации.
                        Пароль пользователя не сериализуется и не отображается во всех респонсах. Также пароль хешируется в базе.
                        Username и Email пользователя уникальны.
                        Для воспроизведения жизненного цикла между "взаимоотношениями" пользователей созданы 2 сущности.
                        Subscription - эмулирует процесс отправки запроса на дружбу и одновременной подписки на пользователя.
                        Есть поле accept, которое указывает на то принята ли заявка в друзья. Если нет, то пользователь так и
                        останется в подписчиках, как и требует задание.
                        FriendShip - сущность, для хранения отношения типа "дружба". Такое разделение необходимо для приведения
                        к более нормальной форме в БД.
                        В обеих сущностях есть композитный первичный ключ, который состоит из 2 пользователей (участников отношений).
                        Данный ключ будет гарантировать уникальность двух внешних ключей на уровне БД.
                        """,
                contact = @Contact(
                        name = "Lesnyak Denis"
                )
        )
)
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
}
