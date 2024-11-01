import React from 'react';
import ReactDOM from "react-dom/client";
import App from './App';
import './index.css';
import { BrowserRouter } from 'react-router-dom';
import {ChakraProvider, extendTheme} from '@chakra-ui/react';
import { Provider } from 'react-redux';
import { store } from './redux/Store';
import { defaultPrivilegeMap, PrivilegesProvider } from './hooks/PrivilegesProvider';
import { AuthProvider } from "./hooks/AuthHook"

const customTheme = extendTheme({
    breakpoints: {
        base: "0em",
        sm: "30em",
        md: "48em",
        lg: "62em",
        xl: "80em",
    },
});

const rootElement = document.getElementById("root");

if (rootElement) {
    ReactDOM.createRoot(rootElement).render(
        // <React.StrictMode>
            <ChakraProvider theme={customTheme}>
                <BrowserRouter>
                    <AuthProvider isAuth={false}>
                        <Provider store={store}>
                            <PrivilegesProvider priv={defaultPrivilegeMap}>
                                <App />
                            </PrivilegesProvider>
                        </Provider>
                    </AuthProvider>
                </BrowserRouter>
            </ChakraProvider>
        // </React.StrictMode>
    );
}
