import * as Yup from "yup"


/**
 * Валидатор формы ролей
 */
export class RoleValidator {
    private static ValidationSchema: Yup.AnySchema = Yup.object()
        .shape({
            NAME: Yup.string()
                .required("Required")
                .min(1)
        })

    public static GetSchema(): Yup.AnySchema {
        return this.ValidationSchema
    }
}