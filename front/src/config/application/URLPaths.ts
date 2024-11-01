import { MapLike } from "typescript";
import { BreadcrumbsData } from "../../shared/entities/Breadcrumbs/BreadCrumbsData";


/**
 * Маппинги названий секций и их путей
 * используются в роутинге 
 */
export const URLPaths: MapLike<BreadcrumbsData> = {
	HOME: {
		link: "/",
		text: "Home"
	},
	AUTH: {
		link: "/auth",
		text: "Authorization"
	},
	CALENDAR: {
		link: "/calendar",
		text: "Common string"
	},
	MENU: {
		link: "/menu",
		text: "Common string of menu"
	},
	GRID: {
		link: "/grid",
		text: "Common string of grid"
	},
	REGFORM: {
		link: "/registration",
		text: "Common string"
	}
	, MODELS: {
		link: "/models",
		text: "Models"
	}
	, MODEL_EDIT: {
		link: "/model/edit",
		text: "Model edit"
	}
	, MODEL_CREATE: {
		link: "/model/create",
		text: "Model create"
	}
	, IN_CONNECTORS: {
		link: "/in/connectors",
		text: "Input connectors"
	}
	, IN_CONNECTOR_EDIT: {
		link: "/in/connector/in/edit",
		text: "Input connector"
	}
	, IN_CONNECTOR_CREATE: {
		link: "/in/connector/create",
		text: "Input connector create"
	}
	, OUT_CONNECTORS: {
		link: "/out/connectors",
		text: "Output connectors"
	}
	, OUT_CONNECTOR_EDIT: {
		link: "/out/connector/edit",
		text: "Output connector edit"
	}
	, OUT_CONNECTOR_CREATE: {
		link: "/out/connector/create",
		text: "Output connector create"
	}
	, RULES: {
		link: "/rules",
		text: "Rules"
	}
	, RULES_EDIT: {
		link: "/rules/edit",
		text: "Rules edit"
	}
	, RULES_CREATE: {
		link: "/rules/create",
		text: "Rules create"
	}
	, DATASTRUCTURE_EDIT: {
		link: "/datastructure/edit",
		text: "Data structure edit"
	}

}

