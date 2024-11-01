import { Box, ChakraProps } from "@chakra-ui/react";
import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { AuthorizationController } from "./controllers/AuthController";
import { useAuth } from "./hooks/AuthHook";
import { useAppSelector } from "./redux/Store";
import { GlobalLoader } from "./components/Loader/GlobalLoader"
import { MainContainer } from "./views/MainContainer";
import { CustomModal } from "./components/Modals/CustomModal";
import { buildPrivilegesMap, defaultPrivilegeMap, usePrivileges } from "./hooks/PrivilegesProvider";
import "dhx-suite/codebase/suite.min.css";
import "@mdi/font/css/materialdesignicons.min.css";
// import {ModalList} from "./components/Modals/ModalList";


/**
 * App.
 *
 * App's main starting poing
 *
 * Renders header and app's main workspace
 * @see Header
 * @see MainContainer
 *
 */
function App() {
	const dispatch = useDispatch()
	const modalData = useAppSelector(store => store.ModalReducer.data)
	const stateModel = useAppSelector(store => store.ModalReducer.stateModel)
	const { isAuth, handleAuthChange } = useAuth()
	const { setPrivileges } = usePrivileges()
	const authController = new AuthorizationController(dispatch)
	const containerStyleConfig: ChakraProps = {}

	useEffect(() => {
		if (isAuth === null || isAuth === undefined) {
			authController.checkIfAuthorized().then(res => {
				if (res.Some.length) {
					setPrivileges(buildPrivilegesMap(res.Some))
					handleAuthChange(true)
					return
				}
				setPrivileges(defaultPrivilegeMap)
				handleAuthChange(false)
			}).catch(err => {
				handleAuthChange(false)
			})
		}
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [isAuth])

	return (
		<Box className="App" {...containerStyleConfig}>
			{isAuth === null || isAuth === undefined ? (
				<GlobalLoader />
			) : (
				<MainContainer isAuth={isAuth} />
			)}
			{modalData && <CustomModal {...modalData} />}
			{/* {stateModel && <ModalList state={stateModel}/>} */}
		</Box>
	);
}

export default App;
