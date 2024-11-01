import { Form, Formik } from "formik";
import React, { FC } from "react";
import { useDispatch } from "react-redux";
import { useLocation, useNavigate } from "react-router";
import { Card } from "../../../components/Card/Card";
import { InputControlStyled } from "../../../components/Inputs/InputControlStyled";
import { LabelStyled } from "../../../components/Inputs/LabelStyled";
import { AuthorizationController } from "../../../controllers/AuthController";
import { RoleController } from "../../../controllers/RoleController";
import { AuthValidator } from "../../../controllers/Validation/AuthValidator";
import { authErrorHandler } from "../../../hooks/AuthHook";
import { buildPrivilegesMap, usePrivileges } from "../../../hooks/PrivilegesProvider";
import { Privileges } from "../../../shared/entities/Role/Privileges";
import { MainButton } from "../../../components/Buttons/MainButton";
import { Box } from "@chakra-ui/react";
import { BoxRowStyleConfig, BoxStyleConfig } from "../FormStyleConfigs";

/**
 * Форма авторизации
 * @returns
 */
export const AuthorizationForm: FC = () => {
	// const location = useLocation();
	// const pathWithoutAuth = location.pathname.replace("/auth", "");

	const navigate = useNavigate()
	const dispath = useDispatch()
	const roleController = new RoleController(dispath)
	const { setPrivileges } = usePrivileges()
	const AuthorizationFormCardConfig = {
		margin: "auto",
		w: {
			sm: "80%",
			md: "50%",
			lg: "50%",
			xl: "30%"
		},
		h: {
			sm: "50%",
			lg: "50%"
		},
		position: "relative",
		justifyContent: "center"
	}

	const authorizationControllerInstance = new AuthorizationController(dispath)
	return (
		<Card {...AuthorizationFormCardConfig} borderWidth="1px" mt="10%" borderRadius="md" p="4">
			{/*            {headingFabric.Generate({type: HeadingType.FORM_HEADING, text: "Authorization"})}*/}
			<Formik initialValues={{ login: "", password: "" }}
				onSubmit={(values: any, actions) => {
					actions.setSubmitting(true)
					console.log("values:", values)
					authorizationControllerInstance.authorize(values).then(res => {
						console.log("auth:", res);
						if (res.Some?.roles.length === 0) {
							actions.setFieldError("login", " ")
							actions.setFieldError("password", "Incorrect login or password")
							actions.setSubmitting(false)
							return
						}

						roleController.getRole(res?.Some?.roles.map(role => Number(role.id))).then(res => {
							if (res?.Some.length === 0) {
								actions.setFieldError("login", " ")
								actions.setFieldError("password", "Incorrect login or password")
								actions.setSubmitting(false)
								return
							}
							console.log("Privileges:", res)
							let privileges: Array<Privileges> = (res?.Some?.map(elem => elem.privileges).flat()).filter((value, index, self) =>
								index === self.findIndex((t) => (
									t && value && t.id === value.id && t.name === value.name
								))
							);
							setPrivileges(buildPrivilegesMap(privileges))
							actions.setSubmitting(false)
							authErrorHandler.handleAuthChange(true)
							// navigate(pathWithoutAuth);
							navigate('/')
							return
						})
					})
				}}
				validationSchema={AuthValidator.GetSchema()}
			>
				{({ isSubmitting, errors, touched, handleSubmit }) => (
					<Form onSubmit={handleSubmit} autoComplete="off">
						<LabelStyled htmlFor='login'>Company Name/E-mail</LabelStyled>
						<InputControlStyled name="login" inputProps={{ maxW: "auto" }} />
						<LabelStyled htmlFor='password'>Password</LabelStyled>
						<InputControlStyled name="password"
							inputProps={{ type: "password", colorScheme: "teal", maxW: "auto" }} />
						<Box {...BoxStyleConfig}>
							<Box {...BoxRowStyleConfig} mt="40px" justifyContent="flex-end">
								<MainButton type="submit" w={"144px"} loadingText="Sent data" isLoading={isSubmitting}>
									Login
								</MainButton>
								{/*                                <MainButton ml={"120px"} text="Forgot password?" textDecoration="underline"
                                                  onClick={() => navigate(URLPaths.RESTORE_PASSWORD.link)}/>*/}
							</Box>
							{/*                            <Box {...BoxRowStyleConfig} mt="10px">
                                <NavLink text="Registration" textDecoration="underline"
                                                  onClick={() => navigate(URLPaths.ACCOUNT_REGISTRATION.link)}/>
                            </Box>*/}
						</Box>
					</Form>
				)}
			</Formik>
		</Card>
	)
}
