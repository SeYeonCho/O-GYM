import React from "react";
import { useState, useEffect } from "react";
import { useRecoilState } from "recoil";
import { AddSNS } from "../../../recoil/atoms/AddSNS";
import styled from "styled-components";
import ReactDOM from "react-dom";
import "antd/dist/antd.css";
import { Button } from "antd";

const StyledInput = styled.input`
  display: block;
  box-sizing: border-box;
  width: 100%;
  border-radius: 4px;
  border: 1px solid black;
  padding: 10px 10px;
  margin-bottom: 10px;
  font-size: 14px;
  color: black;
`;

function AddSns() {
  const [form, setForm] = useState([]);
  const [snsInfo, setsnsInfo] = useRecoilState(AddSNS);

  const prevIsValid = () => {
    if (form.length === 0) {
      return true;
    }

    const someEmpty = form.some(
      (item) => item.SNSUrl === "" || item.SNSPlatform === ""
    );

    if (someEmpty) {
      form.map((item, index) => {
        const allPrev = [...form];

        if (form[index].SNSPlatform === "") {
          allPrev[index].errors.SNSPlatform = "SNSPlatform is required";
        }

        if (form[index].SNSUrl === "") {
          allPrev[index].errors.SNSUrl = "SNSUrl is required";
        }
        setForm(allPrev);
      });
    }

    return !someEmpty;
  };

  const handleAddLink = (e) => {
    e.preventDefault();
    const inputState = {
      SNSPlatform: "",
      SNSUrl: "",

      errors: {
        SNSPlatform: null,
        SNSUrl: null,
      },
    };

    if (prevIsValid()) {
      setForm((prev) => [...prev, inputState]);
    }
    // const newForm = form.map((item, index) => {
    //   return {
    //     platform: item["SNSPlatform"],
    //     SNSUrl: item["SNSUrl"],
    //   };
    // });
    // console.log(newForm);
  };

  const onChange = (index, event) => {
    event.preventDefault();
    event.persist();

    setForm((prev) => {
      return prev.map((item, i) => {
        if (i !== index) {
          return item;
        }

        return {
          ...item,
          [event.target.name]: event.target.value,

          errors: {
            ...item.errors,
            [event.target.name]:
              event.target.value.length > 0
                ? null
                : [event.target.name] + " Is required",
          },
        };
      });
    });
    // setsnsInfo(form);
  };

  useEffect(() => {
    const newForm = form.map((item, index) => {
      return {
        platform: item["SNSPlatform"],
        url: item["SNSUrl"],
      };
    });
    setsnsInfo(newForm);
    // console.log(snsInfo);
  }, [form]);

  const handleRemoveField = (e, index) => {
    e.preventDefault();

    setForm((prev) => prev.filter((item) => item !== prev[index]));
  };
  return (
    <>
      <div>
        <form>
          {form.map((item, index) => (
            <div key={`item-${index}`} style={{ display: "flex" }}>
              {/* <div>
                <StyledInput
                  type="text"
                  name="SNSPlatform"
                  placeholder="SNSPlatform"
                  value={item.SNSPlatform}
                  onChange={(e) => onChange(index, e)}
                />

                {item.errors.SNSPlatform && (
                  <div style={{ color: "red" }}>{item.errors.SNSPlatform}</div>
                )}
              </div> */}
              <div>
                <select
                  name="SNSPlatform"
                  value={item.SNSPlatform}
                  onChange={(e) => onChange(index, e)}
                >
                  <option value="">Select</option>
                  <option value="facebook">페이스북</option>
                  <option value="instagram">인스타그램</option>
                  <option value="twitter">트위터</option>
                  <option value="youtube">유튜브</option>
                </select>
                {item.errors.SNSPlatform && (
                  <div style={{ color: "red" }}>{item.errors.SNSPlatform}</div>
                )}
              </div>

              <div>
                <StyledInput
                  type="text"
                  name="SNSUrl"
                  placeholder="SNSUrl"
                  value={item.SNSUrl}
                  onChange={(e) => onChange(index, e)}
                />

                {item.errors.SNSUrl && (
                  <div style={{ color: "red" }}>{item.errors.SNSUrl}</div>
                )}
              </div>

              <Button
                type="primary"
                danger
                onClick={(e) => handleRemoveField(e, index)}
              >
                X
              </Button>
            </div>
          ))}

          <Button type="primary" onClick={handleAddLink}>
            SNS 추가
          </Button>
        </form>
      </div>
    </>
  );
}

export default AddSns;
