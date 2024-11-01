import * as Yup from "yup"

/**
 * Валидатор формы создания нового пользователя
 */
export class UserValidator {
        private static ValidationSchema: Yup.AnySchema = Yup.object().shape({
                login: Yup.string()
                        .min(1)
                        .matches(new RegExp("[A-Za-z0-9]"), "Логин не может быть пустым")
                        .required("Обязательное поле"),
                EMAIL: Yup.string()
                        .min(1)
                        .matches(new RegExp("[A-Za-z0-9]"), "email не может быть пустым")
                        .required("Обязательное поле"),
                COMPANY: Yup.string()
                        .min(1)
                        .matches(new RegExp("[A-Za-z0-9А-Яа-я]"), "Имя не может быть пустым")
                        .required("Обязательное поле"),
                PHONE: Yup.string()
                        .min(1)
                        .matches(new RegExp("[A-Za-z0-9А-Яа-я]"), "Фамилия не может быть пустой")
                        .required("Обязательное поле"),
        })

        public static GetSchema(): Yup.AnySchema {
                return this.ValidationSchema
        }
}
