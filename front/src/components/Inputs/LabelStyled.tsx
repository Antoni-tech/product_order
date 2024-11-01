import { FormLabel, FormLabelProps } from "@chakra-ui/react";
import { FC } from "react";

/**
 * Лейбл над инпутами стилизованный по умолчанию
 * @param props
 * @returns
 */
export const LabelStyled: FC<FormLabelProps> = (props) => {
	const LabelStyledProps: FormLabelProps = {
		...props,
		paddingTop: "15px",
		fontSize: "16px",
		// fontFamily: "Dosis",
		lineHeight: "20px",
		fontWeight: "400",
		mb: "0px"
	}

	return (
		<FormLabel {...LabelStyledProps} />
	)
}
