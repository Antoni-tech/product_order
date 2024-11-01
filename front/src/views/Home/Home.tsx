import { Flex } from "@chakra-ui/react";
import React, { useState } from "react";
import { Loader } from "../../components/Loader/Loader";
import { Button, Result } from "antd";
import { useNavigate } from "react-router-dom";
import { URLPaths } from "../../config/application/URLPaths";

export const Home: React.FC<{ isAuth: boolean }> = ({ isAuth }) => {
	const [isLoading] = useState(false);
	const navigate = useNavigate();
	return (
		<>
			<Flex flexDir="column" width={"100%"} height={"500px"}>
				{isLoading ? (
					<Loader />
				) : (
					// <>

					//     <Flex flexDir={"column"}
					//           justifyContent={"center"} alignItems={"center"} height={"100%"}>
					//         <Text
					//             color={"gray.600"}
					//             fontSize={{xl: "48px"}}
					//             fontFamily={"Raleway-Regular"}
					//         >
					//             Home page will be here soon
					//         </Text>
					//         <Text
					//             color={"gray.600"}
					//             fontSize={{xl: "48px"}}
					//             fontFamily={"Raleway-Regular"}
					//         >
					//             This page does not exist
					//         </Text>
					//     </Flex>
					// </>

					<>
						{isAuth === false ? <Result
							status="403"
							title="403"
							subTitle="Sorry, you are not authorized to access this page."
							extra={<Button type="primary" onClick={() => navigate(URLPaths.AUTH.link)}>Sign in</Button>}
						/>
							:
							<Result
								status="404"
								title="This page will be here soon"
								subTitle="Sorry, the page you visited does not exist."
							// extra={<Button type="primary">Back Home</Button>}
							/>
						}
					</>

				)}

			</Flex>
		</>
	)
}
