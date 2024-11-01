import * as Yup from 'yup';

export class CommonValidators {
    static commonFieldValidator = Yup.string()
        .matches(/^[A-Za-z0-9~!@#$%^&*()_=\-+><,.|?`\s]+$/, "Field contains invalid characters")
        .min(1, "Field cannot be empty")
        .required("Required field");

    static phoneValidator = Yup.string()
        .matches(/^[0-9+]+$/, 'Field must contain only digits and + symbol')
        .min(5, 'Field must be at least 5 characters')
        .max(15, 'Field must be at most 15 characters')
        .required('Required field');

    static zipValidator = Yup.string()
        .min(1, "Field cannot be empty")
        .matches(/^\d{5}$/, "Field must be a valid ZIP code")
        .required("Required field");

    static passwordValidator = Yup.string()
        .min(4, "Field cannot be empty")
        .matches(/^[ A-Za-z0-9~!@#$%^&*()_=\-+><,.|?`\s]*$/, "Invalid password format")
        .required("Required field");

    static emailValidator = Yup.string()
        .test(
            'multiple-emails',
            'Invalid email format',
            function (value) {
                if (!value) return true;

                const emails = value.split(',').map((email) => email.trim());

                return emails.every((email) => /^[A-Za-z0-9_.+-]+@[A-Za-z0-9-]+\.[A-Za-z0-9-.]+$/.test(email));
            }
        )
        .required('Required field');
}