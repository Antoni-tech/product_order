import { Button, ButtonProps } from "@chakra-ui/react";
import { FC } from "react";

/**
 * Обычная функциональная кнопка, стилизованная по умолчнию
 * @param props
 * @returns
 */

export const MainButton: FC<ButtonProps> = (props) => {
	const ButtonStyleProps: ButtonProps = {
		color: "black",
		height: "35px",
		// fontFamily: "Dosis",
		fontWeight: "500",
		background: "#FFF",
		borderRadius: "6px",
		border: "solid 1px #EFEFEF",
		fontSize: "small",
		...props,
	};
	return (
		<Button {...ButtonStyleProps} />
	)
}
