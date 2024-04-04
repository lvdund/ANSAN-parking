import Register from "./Register";
import Enzyme, { mount } from "enzyme";
import { BrowserRouter as Router } from "react-router-dom";
import { Provider } from "react-redux";
import configureStore from "redux-mock-store";
import Adapter from "@wojtekmaj/enzyme-adapter-react-17";

Enzyme.configure({ adapter: new Adapter() });

const initialState = {
  auth: {
    error: "string",
    loading: false,
  },
};
const mockStore = configureStore();

test("render app", () => {
  const store = mockStore(initialState);
  const wrapper = mount(
    <Provider store={store}>
      <Router>
        <Register />
      </Router>
    </Provider>
  );
  expect(wrapper).toBeTruthy();
});
