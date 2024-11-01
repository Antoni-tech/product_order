import * as Yup from "yup"

/**
 * Валидатор данных формы авторизации 
 */
export class AuthValidator {
    private static ValidationSchema: Yup.AnySchema = Yup.object().shape({
        login: Yup.string()
            .required("Required field"),
        password: Yup.string()
            .matches(/^[ A-Za-z0-9~!@#$%^&*()_=\-+><,.|?`]*$/, "Invalid password format")
            .required("Required field")
    })

    public static GetSchema(): Yup.AnySchema {
        return this.ValidationSchema
    }
}
